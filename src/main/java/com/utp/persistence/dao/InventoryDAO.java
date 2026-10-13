package com.utp.persistence.dao;

import com.utp.config.ConnectionPostgresSQL;
import com.utp.model.dto.CredentialDeliveredDTO;
import com.utp.model.entity.DigitalAccountEntity;
import com.utp.model.entity.DigitalProfileEntity;
import com.utp.model.entity.ProductEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InventoryDAO {
    public boolean registerDigitalAccount(DigitalAccountEntity account, List<DigitalProfileEntity> profiles) {
        boolean exito = false;
        String sqlCuenta = "INSERT INTO cuenta_digital (id_producto, correo_cuenta, contrasena_cifrada, capacidad_perfiles, estado, creado_por) VALUES (?, ?, ?, ?, ?, ?) RETURNING id_cuenta";
        String sqlPerfil = "INSERT INTO perfil_digital (id_cuenta, nombre_perfil, contrasena_perfil_cifrada, estado) VALUES (?, ?, ?, ?)";

        String sqlCheckInv = "SELECT COUNT(*) FROM inventario WHERE id_producto = ?";
        String sqlInsertInv = "INSERT INTO inventario (id_producto, cantidad_disponible, cantidad_reservada, stock_minimo) VALUES (?, ?, 0, 0)";
        String sqlUpdateInv = "UPDATE inventario SET cantidad_disponible = cantidad_disponible + ? WHERE id_producto = ?";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psCuenta = conn.prepareStatement(sqlCuenta)) {
                psCuenta.setInt(1, account.getIdProduct());
                psCuenta.setString(2, account.getAccountEmail());
                psCuenta.setString(3, account.getPaswordHash());
                psCuenta.setInt(4, account.getProfileCapacity());
                psCuenta.setString(5, account.getStatus().name());
                psCuenta.setInt(6, account.getCreatedBy());

                try (ResultSet rs = psCuenta.executeQuery()) {
                    if (rs.next()) {
                        int idCuentaGenerado = rs.getInt(1);

                        // 1. Insertar Perfiles
                        try (PreparedStatement psPerfil = conn.prepareStatement(sqlPerfil)) {
                            for (DigitalProfileEntity profile : profiles) {
                                psPerfil.setInt(1, idCuentaGenerado);
                                psPerfil.setString(2, profile.getProfileName());
                                psPerfil.setString(3, profile.getEncryptedPasswordProfile());
                                psPerfil.setString(4, profile.getDigitalProfileStatus().name());
                                psPerfil.addBatch();
                            }
                            psPerfil.executeBatch();
                        }

                        // 2. Lógica Inteligente de Inventario (Se repara solo)
                        boolean existeInventario = false;
                        try (PreparedStatement psCheck = conn.prepareStatement(sqlCheckInv)) {
                            psCheck.setInt(1, account.getIdProduct());
                            try (ResultSet rsCheck = psCheck.executeQuery()) {
                                if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                                    existeInventario = true;
                                }
                            }
                        }

                        if (existeInventario) {
                            // Si ya existe, sumamos el stock
                            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateInv)) {
                                psUpdate.setInt(1, profiles.size());
                                psUpdate.setInt(2, account.getIdProduct());
                                psUpdate.executeUpdate();
                            }
                        } else {
                            // Si NO existe (productos viejos), lo creamos desde cero con el nuevo stock
                            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsertInv)) {
                                psInsert.setInt(1, account.getIdProduct());
                                psInsert.setInt(2, profiles.size()); // La cantidad disponible inicial son los perfiles que acabamos de crear
                                psInsert.executeUpdate();
                            }
                        }
                    }
                }
                conn.commit();
                exito = true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exito;
    }

    public List<Map<String, Object>> listInventoryAccounts() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT c.id_cuenta, p.nombre as nombre_producto, c.correo_cuenta, " +
                "c.capacidad_perfiles, c.estado " +
                "FROM cuenta_digital c " +
                "INNER JOIN producto p ON c.id_producto = p.id_producto " +
                "ORDER BY c.id_cuenta DESC";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> cuenta = new LinkedHashMap<>();
                cuenta.put("idCuenta", rs.getInt("id_cuenta"));
                cuenta.put("nombreProducto", rs.getString("nombre_producto"));
                cuenta.put("correoCuenta", rs.getString("correo_cuenta"));
                cuenta.put("capacidadPerfiles", rs.getInt("capacidad_perfiles"));
                cuenta.put("estado", rs.getString("estado"));
                lista.add(cuenta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean updateDigitalAccount(DigitalAccountEntity account) {
        boolean exito = false;

        StringBuilder sql = new StringBuilder("UPDATE cuenta_digital SET correo_cuenta = ?, estado = ?");
        // Si el password vino lleno en la entidad, lo actualizamos también
        if (account.getPaswordHash() != null && !account.getPaswordHash().trim().isEmpty()) {
            sql.append(", contrasena_cifrada = ?");
        }
        sql.append(" WHERE id_cuenta = ?");

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setString(1, account.getAccountEmail());
            ps.setString(2, account.getStatus().name());

            int paramIndex = 3;
            if (account.getPaswordHash() != null && !account.getPaswordHash().trim().isEmpty()) {
                ps.setString(paramIndex++, account.getPaswordHash());
            }
            ps.setInt(paramIndex, account.getIdDigitalAcount());

            exito = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exito;
    }

    public boolean deleteDigitalAccount(int idCuenta) {
        boolean exito = false;
        String sqlSelectProducto = "SELECT id_producto FROM cuenta_digital WHERE id_cuenta = ?";
        String sqlCountPerfiles = "SELECT COUNT(*) FROM perfil_digital WHERE id_cuenta = ? AND estado = 'disponible'";
        String sqlDeletePerfiles = "DELETE FROM perfil_digital WHERE id_cuenta = ? AND estado = 'disponible'";
        String sqlRestarInv = "UPDATE inventario SET cantidad_disponible = cantidad_disponible - ? WHERE id_producto = ?";
        String sqlDeleteCuenta = "DELETE FROM cuenta_digital WHERE id_cuenta = ?";
        String sqlSuspendCuenta = "UPDATE cuenta_digital SET estado = 'suspendida' WHERE id_cuenta = ?";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion()) {
            conn.setAutoCommit(false);
            try {
                int idProducto = 0;
                try (PreparedStatement psSel = conn.prepareStatement(sqlSelectProducto)) {
                    psSel.setInt(1, idCuenta);
                    try (ResultSet rs = psSel.executeQuery()) {
                        if (rs.next()) idProducto = rs.getInt(1);
                    }
                }

                int perfilesBorrados = 0;
                try (PreparedStatement psCount = conn.prepareStatement(sqlCountPerfiles)) {
                    psCount.setInt(1, idCuenta);
                    try (ResultSet rs = psCount.executeQuery()) {
                        if (rs.next()) perfilesBorrados = rs.getInt(1);
                    }
                }

                if (perfilesBorrados > 0) {
                    try (PreparedStatement psDelPerf = conn.prepareStatement(sqlDeletePerfiles)) {
                        psDelPerf.setInt(1, idCuenta);
                        psDelPerf.executeUpdate();
                    }
                    try (PreparedStatement psRestarInv = conn.prepareStatement(sqlRestarInv)) {
                        psRestarInv.setInt(1, perfilesBorrados);
                        psRestarInv.setInt(2, idProducto);
                        psRestarInv.executeUpdate();
                    }
                }

                try (PreparedStatement psDelCuenta = conn.prepareStatement(sqlDeleteCuenta)) {
                    psDelCuenta.setInt(1, idCuenta);
                    psDelCuenta.executeUpdate();
                } catch (SQLException ex) {
                    try (PreparedStatement psSusp = conn.prepareStatement(sqlSuspendCuenta)) {
                        psSusp.setInt(1, idCuenta);
                        psSusp.executeUpdate();
                    }
                }

                conn.commit();
                exito = true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exito;
    }

    public boolean createProduct(ProductEntity producto) {
        boolean exito = false;

        // 1. Insertar el producto y obtener el ID generado
        String sqlProducto = "INSERT INTO producto (id_categoria, id_tipo_producto, nombre, descripcion, imagen_url, precio, estado, creado_por) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_producto";

        // 2. Insertar el inventario inicial en 0 para ese nuevo ID
        String sqlInventario = "INSERT INTO inventario (id_producto, cantidad_disponible, cantidad_reservada, stock_minimo) " +
                "VALUES (?, 0, 0, 0)";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion()) {
            // Iniciar transacción
            conn.setAutoCommit(false);

            try (PreparedStatement psProd = conn.prepareStatement(sqlProducto)) {
                psProd.setInt(1, producto.getIdCategory());
                psProd.setInt(2, producto.getIdProductType());
                psProd.setString(3, producto.getName());
                psProd.setString(4, producto.getDescription());
                psProd.setString(5, producto.getUrlImage());
                psProd.setDouble(6, producto.getPrice());
                psProd.setString(7, producto.getProductStatus().name());
                psProd.setInt(8, producto.getCreatedBy());

                try (ResultSet rs = psProd.executeQuery()) {
                    if (rs.next()) {
                        int idProductoGenerado = rs.getInt(1);

                        // Crear el registro de inventario en 0
                        try (PreparedStatement psInv = conn.prepareStatement(sqlInventario)) {
                            psInv.setInt(1, idProductoGenerado);
                            psInv.executeUpdate();
                        }
                    }
                }

                conn.commit();
                exito = true;

            } catch (SQLException e) {
                // Si falla algo, deshacer los cambios
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exito;
    }

    public List<CredentialDeliveredDTO> getCredentialsByClient(int idClient) {
        List<CredentialDeliveredDTO> lista = new ArrayList<>();
        String sql = "SELECT v.numero_venta, p.nombre AS nombre_producto, " +
                "cd.correo_cuenta, cd.contrasena_cifrada, " + // <-- Usando el nombre exacto de tu BD
                "pd.nombre_perfil, pd.contrasena_perfil_cifrada " +
                "FROM asignacion_producto ap " +
                "INNER JOIN detalle_venta dv ON ap.id_detalle = dv.id_detalle " +
                "INNER JOIN venta v ON dv.id_venta = v.id_venta " +
                "INNER JOIN producto p ON dv.id_producto = p.id_producto " +
                "LEFT JOIN perfil_digital pd ON ap.id_perfil = pd.id_perfil " +
                "LEFT JOIN cuenta_digital cd ON ap.id_cuenta = cd.id_cuenta OR pd.id_cuenta = cd.id_cuenta " +
                "WHERE v.id_cliente = ?";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CredentialDeliveredDTO dto = new CredentialDeliveredDTO(
                            rs.getString("numero_venta"),
                            rs.getString("nombre_producto"),
                            rs.getString("correo_cuenta"),
                            rs.getString("contrasena_cifrada"),
                            rs.getString("nombre_perfil"),
                            rs.getString("contrasena_perfil_cifrada")
                    );
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
