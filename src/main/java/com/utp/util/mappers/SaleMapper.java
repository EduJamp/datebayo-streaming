package com.utp.util.mappers;

import com.utp.model.dto.SaleDTO;
import com.utp.util.enums.PayStatus;
import com.utp.util.enums.SalesStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class SaleMapper {
    public static SaleDTO toSaleDTO(ResultSet rs) throws SQLException {
        String fecha = null;
        if (rs.getTimestamp("fecha_creacion") != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            fecha = sdf.format(rs.getTimestamp("fecha_creacion"));
        }

        String voucherFileName = rs.getString("voucher_file_name");
        if (voucherFileName == null || voucherFileName.isBlank()) {
            voucherFileName = "default.jpg";
        }

        String estadoVenta = rs.getString("estado_venta");
        String estadoPago = rs.getString("estado_pago");

        return new SaleDTO(
                rs.getInt("id_venta"),
                rs.getString("numero_venta"),
                rs.getString("nombres_cliente") + " " + rs.getString("apellidos_cliente"),
                rs.getString("correo"),
                rs.getDouble("total"),
                estadoVenta != null ? SalesStatus.valueOf(estadoVenta) : SalesStatus.pendiente_pago,
                rs.getString("metodo_pago"),
                estadoPago != null ? PayStatus.valueOf(estadoPago) : PayStatus.pendiente,
                fecha,
                voucherFileName
        );
    }
}
