package com.utp.persistence.dao.interfaces_dao;

import com.utp.model.entity.RolEntity;

import java.sql.SQLException;
import java.util.List;

public interface IRolDAO {
    boolean createRol(RolEntity entity) throws SQLException;
    List<RolEntity> getAllRol()  throws SQLException;
    boolean updateRol(RolEntity entity) throws SQLException;
    boolean deleteRol(int idRol) throws SQLException;
    List<RolEntity> searchRol(String criterio) throws SQLException;
}
