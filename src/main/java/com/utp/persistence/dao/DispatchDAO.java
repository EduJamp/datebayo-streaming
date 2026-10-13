package com.utp.persistence.dao;

import com.utp.config.ConnectionPostgresSQL;
import com.utp.model.dto.CredentialDeliveredDTO;
import com.utp.util.mappers.DispatchMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DispatchDAO {
    public List<CredentialDeliveredDTO> getCredentialForSale(int idSale) {
        List<CredentialDeliveredDTO> list = new ArrayList<>();

        String sql = "SELECT v.numero_venta, dv.nombre_producto_snapshot AS nombre_producto, " +
                "cd.correo_cuenta, pd.nombre_perfil, pd.contrasena_perfil_cifrada " +
                "FROM venta v " +
                "INNER JOIN detalle_venta dv ON v.id_venta = dv.id_venta " +
                "INNER JOIN asignacion_producto ap ON dv.id_detalle = ap.id_detalle " +
                "INNER JOIN perfil_digital pd ON ap.id_perfil = pd.id_perfil " +
                "INNER JOIN cuenta_digital cd ON pd.id_cuenta = cd.id_cuenta " +
                "WHERE v.id_venta = ? AND ap.estado IN ('asignada', 'entregada')";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSale);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(DispatchMapper.toCredentialDeliveredDTO(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean assignAutomaticProfileToSale(int idSale, int idEmployeeValidator) {
        boolean exito = false;
        String sqlDetalles = "SELECT id_detalle, id_producto, cantidad FROM detalle_venta WHERE id_venta = ?";
        String sqlBuscarPerfil = "SELECT p.id_perfil FROM perfil_digital p INNER JOIN cuenta_digital c ON p.id_cuenta = c.id_cuenta " +
                "WHERE c.id_producto = ? AND p.estado = 'disponible' LIMIT 1";
        String sqlUpdatePerfil = "UPDATE perfil_digital SET estado = 'asignado', fecha_asignacion = CURRENT_TIMESTAMP WHERE id_perfil = ?";
        String sqlAsignar = "INSERT INTO asignacion_producto (id_detalle, id_perfil, estado) VALUES (?, ?, 'asignada')";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psDetalles = conn.prepareStatement(sqlDetalles);
                 PreparedStatement psBuscar = conn.prepareStatement(sqlBuscarPerfil);
                 PreparedStatement psUpdPerfil = conn.prepareStatement(sqlUpdatePerfil);
                 PreparedStatement psAsignar = conn.prepareStatement(sqlAsignar)) {

                psDetalles.setInt(1, idSale);
                ResultSet rsDetalles = psDetalles.executeQuery();

                // Recorremos los productos comprados en esa venta
                while (rsDetalles.next()) {
                    int idDetalle = rsDetalles.getInt("id_detalle");
                    int idProducto = rsDetalles.getInt("id_producto");
                    int cantidad = rsDetalles.getInt("cantidad");

                    // Si compró 2 pantallas, el bucle gira 2 veces
                    for (int i = 0; i < cantidad; i++) {
                        psBuscar.setInt(1, idProducto);
                        ResultSet rsPerfil = psBuscar.executeQuery();

                        if (rsPerfil.next()) {
                            int idPerfilDisponible = rsPerfil.getInt("id_perfil");

                            // Cambiar estado del perfil a 'asignado'
                            psUpdPerfil.setInt(1, idPerfilDisponible);
                            psUpdPerfil.executeUpdate();

                            // Vincular el perfil al detalle de la venta
                            psAsignar.setInt(1, idDetalle);
                            psAsignar.setInt(2, idPerfilDisponible);
                            psAsignar.executeUpdate();
                        } else {
                            throw new SQLException("No hay perfiles disponibles para el producto ID: " + idProducto);
                        }
                    }
                }
                conn.commit();
                exito = true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return exito;
    }
}
