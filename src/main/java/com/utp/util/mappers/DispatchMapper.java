package com.utp.util.mappers;

import com.utp.model.dto.CredentialDeliveredDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

// Despacho
public class DispatchMapper {
    public static CredentialDeliveredDTO toCredentialDeliveredDTO(ResultSet rs) throws SQLException {
        return new CredentialDeliveredDTO(
                rs.getString("numero_venta"),
                rs.getString("nombre_producto"),
                rs.getString("correo_cuenta"),
                rs.getString("contrasena_cuenta"),
                rs.getString("nombre_perfil"),
                rs.getString("contrasena_perfil_cifrada")
        );

    }
}
