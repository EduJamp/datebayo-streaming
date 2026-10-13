package com.utp.util.mappers;

import com.utp.model.dto.RolDTO;
import com.utp.model.entity.RolEntity;

public class RolMapper {
    public static RolEntity toEntity(RolDTO dto) {
        return new RolEntity(
                dto.idRol(),
                dto.rolName(),
                dto.rolDescription()
        );
    }

    public static RolDTO toDTO(RolEntity entity) {
        return new RolDTO(
                entity.getIdRol(),
                entity.getRolName(),
                entity.getRolDescription()
        );
    }
}
