package com.utp.persistence.dao;

import com.utp.config.ConnectionPostgresSQL;
import com.utp.model.entity.RolEntity;
import com.utp.persistence.dao.interfaces_dao.IRolDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO implements IRolDAO {

    public RolDAO() {
    }

    @Override
    public boolean createRol(RolEntity entity) throws SQLException {
        String sql = "insert Rol( nombre, descripcion ) values ( ?, ? ) ";

        try ( Connection con = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS ) ) {
            ps.setString( 1, entity.getRolName() );
            ps.setString( 2, entity.getRolDescription() );

            int rowAffected = ps.executeUpdate();

            if ( rowAffected > 0 ) {
                try ( ResultSet rs = ps.getGeneratedKeys() ) {
                    if ( rs.next() ) {
                        entity.setIdRol( rs.getInt( 1 ) );
                    }
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public List<RolEntity> getAllRol() throws SQLException {
        String sql = "select * from Rol";

        List<RolEntity> roles = new ArrayList<>();

        try ( Connection con = ConnectionPostgresSQL.getInstancia().getConexion();
        PreparedStatement ps = con.prepareStatement( sql ) ) {
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ) {
                roles.add( mapearRol( rs ) );
            }
        }
        return roles;
    }

    @Override
    public boolean updateRol(RolEntity entity) throws SQLException {
        String sql = "update Rol set nombre=?, descripcion=? where id_rol=?";

        try ( Connection con = ConnectionPostgresSQL.getInstancia().getConexion();
        PreparedStatement ps = con.prepareStatement( sql ) ) {
            ps.setString( 1, entity.getRolName() );
            ps.setString( 2, entity.getRolDescription() );
            ps.setInt( 3, entity.getIdRol() );

            int rowAffected = ps.executeUpdate();

            return rowAffected > 0;
        }
    }

    @Override
    public boolean deleteRol(int idRol) throws SQLException {
        String sql = "delete from Rol where id_rol=?";

        try ( Connection con = ConnectionPostgresSQL.getInstancia().getConexion();
        PreparedStatement ps = con.prepareStatement( sql ) ) {
            ps.setInt( 1, idRol );

            int rowAffected = ps.executeUpdate();

            return rowAffected > 0;
        }
    }

    @Override
    public List<RolEntity> searchRol(String criterio) throws SQLException {
        String sql = "select * from Categoria where CAST(id_rol as text) ilike ? or nombre ilike ? " +
                "order by id_rol desc";

        List<RolEntity> roles = new ArrayList<>();

        try ( Connection con = ConnectionPostgresSQL.getInstancia().getConexion();
        PreparedStatement ps = con.prepareStatement( sql ) ) {
            String patron = "%" + criterio + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);

            try ( ResultSet rs = ps.executeQuery() ) {
                while ( rs.next() ) {
                    roles.add( mapearRol( rs ) );
                }
            }
        }
        return roles;
    }

    private RolEntity mapearRol(ResultSet rs) throws SQLException {
        RolEntity entity = new RolEntity();

        entity.setIdRol( rs.getInt( "id_rol" ) );
        entity.setRolName( rs.getString( "nombre" ) );
        entity.setRolDescription( rs.getString( "descripcion" ) );

        return entity;
    }
}
