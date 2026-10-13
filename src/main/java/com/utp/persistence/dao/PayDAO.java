package com.utp.persistence.dao;

import com.utp.config.ConnectionPostgresSQL;
import com.utp.model.entity.PayEntity;
import com.utp.model.entity.VoucherPayEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PayDAO {
    public boolean registerVoucher(int idSale, PayEntity pay, VoucherPayEntity voucher) {
        boolean exito = false;
        String sqlPago = "INSERT INTO pago (id_venta, metodo_pago, monto, estado) VALUES (?, ?, ?, 'en_revision') RETURNING id_pago";
        String sqlComprobante = "INSERT INTO comprobante_pago (id_pago, url_archivo, nombre_archivo) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psPago = conn.prepareStatement(sqlPago)) {
                psPago.setInt(1, idSale);
                psPago.setString(2, pay.getPaymentMethod());
                psPago.setDouble(3, pay.getAmount());

                try (ResultSet rs = psPago.executeQuery()) {
                    if (rs.next()) {
                        int idPagoGenerado = rs.getInt(1);

                        try (PreparedStatement psComp = conn.prepareStatement(sqlComprobante)) {
                            psComp.setInt(1, idPagoGenerado);
                            psComp.setString(2, voucher.getFileUrl());
                            psComp.setString(3, voucher.getFileName());
                            psComp.executeUpdate();
                        }

                        try (PreparedStatement psUpdVenta = conn.prepareStatement("UPDATE venta SET estado = 'pago_en_revision' WHERE id_venta = ?")) {
                            psUpdVenta.setInt(1, idSale);
                            psUpdVenta.executeUpdate();
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

    public boolean validatePayment(int idPago, int idEmpleadoValidador, String resultado) {
        boolean exito = false;
        String estadoPago = resultado.equals("aprobado") ? "aprobado" : "rechazado";

        String sqlUpdatePago = "UPDATE pago SET estado = ?, validado_por = ?, fecha_validacion = CURRENT_TIMESTAMP WHERE id_pago = ?";
        String sqlInsertValidacion = "INSERT INTO validacion_pago (id_pago, id_usuario_validador, resultado) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psPago = conn.prepareStatement(sqlUpdatePago);
                 PreparedStatement psValidacion = conn.prepareStatement(sqlInsertValidacion)) {

                psPago.setString(1, estadoPago);
                psPago.setInt(2, idEmpleadoValidador);
                psPago.setInt(3, idPago);
                psPago.executeUpdate();

                psValidacion.setInt(1, idPago);
                psValidacion.setInt(2, idEmpleadoValidador);
                psValidacion.setString(3, resultado);
                psValidacion.executeUpdate();

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
