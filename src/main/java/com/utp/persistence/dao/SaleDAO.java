package com.utp.persistence.dao;

import com.utp.config.ConnectionPostgresSQL;
import com.utp.model.dto.CartDTO;
import com.utp.model.dto.SaleDTO;
import com.utp.model.dto.ShoppingCartItemDTO;
import com.utp.model.entity.SaleEntity;
import com.utp.util.enums.PayStatus;
import com.utp.util.enums.SalesStatus;
import com.utp.util.mappers.SaleMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Venta
public class SaleDAO {
    public int registerSale(int idClient, CartDTO cart, double discount) {
        int idVentaGenerado = -1;
        String numVenta = "VT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        String sqlVenta = "INSERT INTO venta (numero_venta, id_cliente, estado, subtotal, descuento_total, total) VALUES (?, ?, 'pendiente_pago', ?, ?, ?) RETURNING id_venta";
        String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, nombre_producto_snapshot, precio_unitario, cantidad, subtotal, descuento, total_linea) VALUES (?, ?, ?, ?, ?, ?, 0, ?)";
        String sqlInventario = "UPDATE inventario SET cantidad_disponible = cantidad_disponible - ? WHERE id_producto = ? AND cantidad_disponible >= ?";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement psVenta = conn.prepareStatement(sqlVenta)) {
                    psVenta.setString(1, numVenta);
                    psVenta.setInt(2, idClient);
                    psVenta.setDouble(3, cart.getTotal());
                    psVenta.setDouble(4, discount);
                    psVenta.setDouble(5, cart .getTotal() - discount);

                    try (ResultSet rs = psVenta.executeQuery()) {
                        if (rs.next()) {
                            idVentaGenerado = rs.getInt(1);
                        }
                    }
                }

                if (idVentaGenerado == -1) throw new SQLException("Fallo al crear la venta padre");

                try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle);
                     PreparedStatement psInventario = conn.prepareStatement(sqlInventario)) {

                    for (ShoppingCartItemDTO item : cart.getItems()) {
                        psDetalle.setInt(1, idVentaGenerado);
                        psDetalle.setInt(2, item.getIdProduct());
                        psDetalle.setString(3, item.getName());
                        psDetalle.setDouble(4, item.getUnitPrice());
                        psDetalle.setInt(5, item.getQuantity());
                        psDetalle.setDouble(6, item.getSubtotal());
                        psDetalle.setDouble(7, item.getSubtotal());
                        psDetalle.executeUpdate();

                        psInventario.setInt(1, item.getQuantity());
                        psInventario.setInt(2, item.getIdProduct());
                        psInventario.setInt(3, item.getQuantity());

                        int filasAfectadas = psInventario.executeUpdate();
                        if (filasAfectadas == 0) {
                            throw new SQLException("Stock insuficiente para el producto: " + item.getName());
                        }
                    }
                }
                conn.commit();

            } catch (SQLException e) {
                conn.rollback(); // Si falta stock o hay error, no se guarda NADA.
                e.printStackTrace();
                idVentaGenerado = -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idVentaGenerado;
    }

    public List<SaleEntity> viewPurchaseHistory(int idClient) throws SQLException {

        List<SaleEntity> history = new java.util.ArrayList<>();

        String sql = "SELECT id_venta, numero_venta, id_cliente, " +
                "id_empleado_responsable, estado, subtotal, descuento_total, " +
                "total, fecha_creacion, fecha_confirmacion, observaciones " +
                "FROM venta " +
                "WHERE id_cliente = ? " +
                "ORDER BY fecha_creacion DESC";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idClient);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    SaleEntity sale = new SaleEntity();

                    // Identificación de la venta y del cliente.
                    sale.setIdSale(rs.getInt("id_venta"));
                    sale.setSalesNumber(rs.getString("numero_venta"));
                    sale.setIdClient(rs.getInt("id_cliente"));

                    // Empleado responsable
                    int idEmpleado = rs.getInt("id_empleado_responsable");

                    if (rs.wasNull()) {
                        sale.setIdEmployee(null);
                    } else {
                        sale.setIdEmployee(idEmpleado);
                    }

                    // Estado de la venta.
                    String estado = rs.getString("estado");

                    if (estado != null) {
                        sale.setSaleStatus(
                                com.utp.util.enums.SalesStatus.valueOf(
                                        estado.toLowerCase(
                                                java.util.Locale.ROOT
                                        )
                                )
                        );
                    }

                    sale.setSubtotal(rs.getDouble("subtotal"));
                    sale.setTotalDiscount(rs.getDouble("descuento_total"));
                    sale.setTotal(rs.getDouble("total"));

                    sale.setCreationDate(
                            rs.getTimestamp("fecha_creacion").toLocalDateTime()
                    );

                    java.sql.Timestamp fechaConfirmacion =
                            rs.getTimestamp("fecha_confirmacion");

                    sale.setConfirmationDate(
                            fechaConfirmacion != null
                                    ? fechaConfirmacion.toLocalDateTime()
                                    : null
                    );

                    sale.setObservation(rs.getString("observaciones"));

                    history.add(sale);
                }
            }
        }

        return history;
    }

    public List<SaleDTO> listGlobalSales(String estate) {
        List<SaleDTO> list = new ArrayList<>();
        String sql = "SELECT v.id_venta, v.numero_venta, u.nombres AS nombres_cliente, u.apellidos AS apellidos_cliente, " +
                "u.correo, v.total, v.estado AS estado_venta, p.metodo_pago, p.estado AS estado_pago, v.fecha_creacion, " +
                "cp.nombre_archivo AS voucher_file_name " +
                "FROM venta v " +
                "INNER JOIN usuario u ON v.id_cliente = u.id_usuario " +
                "LEFT JOIN pago p ON v.id_venta = p.id_venta " +
                "LEFT JOIN comprobante_pago cp ON p.id_pago = cp.id_pago " +
                "WHERE v.estado = ? ORDER BY v.fecha_creacion DESC";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String voucher = rs.getString("voucher_file_name");
                    if (voucher == null || voucher.isBlank()) {
                        voucher = "default.jpg";
                    }

                    SaleDTO dto = new SaleDTO(
                            rs.getInt("id_venta"),
                            rs.getString("numero_venta"),
                            rs.getString("nombres_cliente") + " " + rs.getString("apellidos_cliente"),
                            rs.getString("correo"),
                            rs.getDouble("total"),
                            SalesStatus.fromString(rs.getString("estado_venta")),
                            rs.getString("metodo_pago"),
                            PayStatus.fromString(rs.getString("estado_pago")),
                            rs.getString("fecha_creacion"),
                            voucher
                    );
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SaleDTO> listSaleByEmployee(int idEmployee) {
        List<SaleDTO> lista = new ArrayList<>();
        String sql = "SELECT v.id_venta, v.numero_venta, u.nombres AS nombres_cliente, u.apellidos AS apellidos_cliente, " +
                "u.correo, v.total, v.estado AS estado_venta, p.metodo_pago, p.estado AS estado_pago, v.fecha_creacion, " +
                "cp.nombre_archivo AS voucher_file_name " +
                "FROM venta v " +
                "INNER JOIN usuario u ON v.id_cliente = u.id_usuario " +
                "LEFT JOIN pago p ON v.id_venta = p.id_venta " +
                "LEFT JOIN comprobante_pago cp ON p.id_pago = cp.id_pago " +
                "WHERE v.id_empleado_responsable = ? ORDER BY v.fecha_creacion DESC";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmployee);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String voucher = rs.getString("voucher_file_name");
                    if (voucher == null || voucher.isBlank()) {
                        voucher = "default.jpg";
                    }

                    SaleDTO dto = new SaleDTO(
                            rs.getInt("id_venta"),
                            rs.getString("numero_venta"),
                            rs.getString("nombres_cliente") + " " + rs.getString("apellidos_cliente"),
                            rs.getString("correo"),
                            rs.getDouble("total"),
                            SalesStatus.fromString(rs.getString("estado_venta")),
                            rs.getString("metodo_pago"),
                            PayStatus.fromString(rs.getString("estado_pago")),
                            rs.getString("fecha_creacion"),
                            voucher
                    );
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean changeSalesStatusAndAssingEmployee(int idVenta, String nuevoEstado, int idUsuarioValidador) {
        String sqlGetEmpleado = "SELECT id_empleado FROM empleado WHERE id_usuario = ?";
        String sqlInsertEmpleado = "INSERT INTO empleado (id_usuario, codigo_empleado, fecha_ingreso, estado_laboral) VALUES (?, ?, CURRENT_DATE, 'activo') RETURNING id_empleado";
        String sqlUpdateVenta = "UPDATE venta SET estado = ?, id_empleado_responsable = ? WHERE id_venta = ?";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion()) {
            conn.setAutoCommit(false);

            try {
                int idEmpleadoReal = -1;

                // Paso 1: Verificar si el usuario ya es empleado en la tabla 'empleado'
                try (PreparedStatement psCheck = conn.prepareStatement(sqlGetEmpleado)) {
                    psCheck.setInt(1, idUsuarioValidador);
                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (rs.next()) {
                            idEmpleadoReal = rs.getInt("id_empleado");
                        }
                    }
                }

                // Paso 2: Si no existe en la tabla empleado, lo registramos automáticamente
                if (idEmpleadoReal == -1) {
                    try (PreparedStatement psInsertEmp = conn.prepareStatement(sqlInsertEmpleado)) {
                        psInsertEmp.setInt(1, idUsuarioValidador);
                        psInsertEmp.setString(2, "EMP-" + String.format("%03d", idUsuarioValidador));
                        try (ResultSet rsIns = psInsertEmp.executeQuery()) {
                            if (rsIns.next()) {
                                idEmpleadoReal = rsIns.getInt(1);
                            }
                        }
                    }
                }

                // Paso 3: Actualizar la venta con el id_empleado correcto
                try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateVenta)) {
                    psUpdate.setString(1, nuevoEstado);
                    psUpdate.setInt(2, idEmpleadoReal);
                    psUpdate.setInt(3, idVenta);
                    int filas = psUpdate.executeUpdate();

                    if (filas > 0) {
                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
