package com.utp.util.mappers;

import com.utp.model.dto.UserSesionDTO;
import com.utp.model.entity.UserEntity;
import com.utp.util.enums.UserStatus;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserMapper {
    public static UserSesionDTO toDTO(ResultSet rs) throws SQLException {
        String rolesStr = rs.getString("roles_asignados");

        List<String> rolesList = Optional.ofNullable(rolesStr)
                .filter(str -> !str.isEmpty())
                .map(str -> Arrays.stream(str.split(","))
                        .map(String::trim)
                        .toList())
                .orElseGet(ArrayList::new);

        return new UserSesionDTO(
                rs.getInt("id_usuario"),
                rs.getString("nombres"),
                rs.getString("correo"),
                rolesList
        );
    }

    public static UserEntity toEntity(ResultSet rs) throws SQLException {
        UserEntity user = new UserEntity();
        user.setIdUser( rs.getInt( "id_usuario"));
        user.setName( rs.getString( "nombres"));
        user.setLastname( rs.getString( "apellidos"));
        user.setEmail( rs.getString( "correo" ) );
        user.setCellphone( rs.getString( "telefono" ) );
        String userState = rs.getString("estado");
        if (userState != null) {
            user.setUserStatus(UserStatus.valueOf(userState));
        }

        user.setRegistrationDate(
                rs.getObject("fecha_registro", LocalDateTime.class)
        );

        user.setLastAccessDate(
                rs.getObject("ultimo_acceso", LocalDateTime.class)
        );

        return user;
    }
}
