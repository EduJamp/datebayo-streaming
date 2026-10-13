package com.utp.persistence.dao;

import com.utp.config.ConnectionPostgresSQL;
import com.utp.model.dto.ClienteDTO;
import com.utp.model.dto.UserSesionDTO;
import com.utp.model.entity.EmployeeEntity;
import com.utp.model.entity.UserEntity;
import com.utp.util.mappers.UserMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDAO {
    public boolean registerClient(UserEntity entity) throws SQLException {
        boolean exito = false;

        String sqlUser = "INSERT INTO usuario (nombres, apellidos, correo, password_hash, telefono, estado, fecha_registro, ultimo_acceso) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlRole = "INSERT INTO usuario_rol (id_usuario, id_rol) " +
                "VALUES (?, (SELECT id_rol FROM rol WHERE nombre = 'CLIENTE'))";

        try (Connection con = ConnectionPostgresSQL.getInstancia().getConexion()) {
            con.setAutoCommit(false); // Iniciamos transacción

            try (PreparedStatement psUser = con.prepareStatement(sqlUser, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, entity.getName());
                psUser.setString(2, entity.getLastname());
                psUser.setString(3, entity.getEmail());
                psUser.setString(4, entity.getPasswordHash());
                psUser.setString(5, entity.getCellphone());
                psUser.setString(6, entity.getUserStatus().name());
                psUser.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
                psUser.setTimestamp(8, new java.sql.Timestamp(System.currentTimeMillis()));

                int rowAffected = psUser.executeUpdate();

                if (rowAffected > 0) {
                    try (ResultSet rs = psUser.getGeneratedKeys()) {
                        if (rs.next()) {
                            int idGenerado = rs.getShort(1);

                            try (PreparedStatement psRol = con.prepareStatement(sqlRole)) {
                                psRol.setInt(1, idGenerado);
                                psRol.executeUpdate();
                            }
                        }
                    }
                }

                con.commit();
                exito = true; // Finalizamos transaccion

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
        return exito;
    }

    public UserSesionDTO login(String email, String password) throws SQLException {
        UserSesionDTO dto = null;

        String sql = "SELECT u.id_usuario, u.nombres, u.correo, " +
                "STRING_AGG(r.nombre, ',') AS roles_asignados " +
                "FROM usuario u " +
                "INNER JOIN usuario_rol ur ON u.id_usuario = ur.id_usuario " +
                "INNER JOIN rol r ON ur.id_rol = r.id_rol " +
                "WHERE u.correo = ? AND u.password_hash = ? AND u.estado = 'activo' " +
                "GROUP BY u.id_usuario, u.nombres, u.correo";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto = UserMapper.toDTO(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dto;
    }

    public boolean registerEmployee(UserEntity userEntity, EmployeeEntity employeeEntity, int idAdminCreator) throws SQLException {
        boolean exito = false;

        // Registro base del usuario
        String sqlUser = "INSERT INTO usuario (nombres, apellidos, correo, password_hash, telefono, estado, fecha_registro, ultimo_acceso) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Asignación del rol Empleado
        String sqlRole = "INSERT INTO usuario_rol (id_usuario, id_rol) " +
                "VALUES (?, (SELECT id_rol FROM rol WHERE nombre = 'EMPLEADO'))";

        // Datos específicos del empleado
        String sqlEmployee = "INSERT INTO empleado (id_usuario, codigo_empleado, fecha_ingreso, estado_laboral, creado_por) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectionPostgresSQL.getInstancia().getConexion()) {
            con.setAutoCommit(false); // Iniciamos una única transacción atómica

            try (PreparedStatement psUser = con.prepareStatement(sqlUser, PreparedStatement.RETURN_GENERATED_KEYS)) {

                psUser.setString(1, userEntity.getName());
                psUser.setString(2, userEntity.getLastname());
                psUser.setString(3, userEntity.getEmail());
                psUser.setString(4, userEntity.getPasswordHash());
                psUser.setString(5, userEntity.getCellphone());
                psUser.setString(6, userEntity.getUserStatus().name());
                psUser.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
                psUser.setTimestamp(8, new java.sql.Timestamp(System.currentTimeMillis()));

                int userRows = psUser.executeUpdate();

                if (userRows > 0) {
                    try (ResultSet rs = psUser.getGeneratedKeys()) {
                        if (rs.next()) {
                            int idUsuarioGenerado = rs.getInt(1);

                            try (PreparedStatement psRol = con.prepareStatement(sqlRole)) {
                                psRol.setInt(1, idUsuarioGenerado);
                                psRol.executeUpdate();
                            }

                            try (PreparedStatement psEmp = con.prepareStatement(sqlEmployee)) {
                                psEmp.setInt(1, idUsuarioGenerado);
                                psEmp.setString(2, employeeEntity.getEmployeeCode());

                                if (employeeEntity.getDateOfEntry() != null) {
                                    psEmp.setDate(3, new java.sql.Date(employeeEntity.getDateOfEntry().getTime()));
                                } else {
                                    psEmp.setNull(3, java.sql.Types.DATE);
                                }

                                psEmp.setString(4, employeeEntity.getEmployementStatus().name());

                                psEmp.setInt(5, idAdminCreator);

                                psEmp.executeUpdate();
                            }
                        }
                    }
                }

                con.commit(); // Confirmamos los 3 inserts juntos
                exito = true;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
        return exito;
    }

    public UserEntity findByEmail(String email) throws SQLException {

        String sql = "SELECT id_usuario, nombres, apellidos, correo, " +
                "password_hash, telefono, estado " +
                "FROM usuario WHERE LOWER(correo) = LOWER(?)";

        try (Connection con = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    UserEntity usuario = new UserEntity();

                    usuario.setIdUser(rs.getInt("id_usuario"));
                    usuario.setName(rs.getString("nombres"));
                    usuario.setLastname(rs.getString("apellidos"));
                    usuario.setEmail(rs.getString("correo"));
                    usuario.setPasswordHash(rs.getString("password_hash"));
                    usuario.setCellphone(rs.getString("telefono"));

                    String estado = rs.getString("estado");

                    if (estado != null) {
                        usuario.setUserStatus(
                                com.utp.util.enums.UserStatus.valueOf(
                                        estado.toLowerCase(java.util.Locale.ROOT)
                                )
                        );
                    }

                    return usuario;
                }
            }
        }

        return null;
    }

    public List<Map<String,Object>> listEmployees() throws SQLException {
        List<Map<String,Object>> out = new ArrayList<>();
        String sql = "SELECT e.id_empleado, e.id_usuario, e.codigo_empleado, e.fecha_ingreso, e.estado_laboral, u.nombres, u.apellidos, u.correo, u.telefono, u.estado AS estado_usuario FROM empleado e JOIN usuario u ON u.id_usuario=e.id_usuario ORDER BY e.id_empleado DESC";
        try (Connection c=ConnectionPostgresSQL.getInstancia().getConexion(); PreparedStatement ps=c.prepareStatement(sql); ResultSet rs=ps.executeQuery()) {
            while(rs.next()) { Map<String,Object> m=new LinkedHashMap<>();
                m.put("idEmpleado",rs.getInt("id_empleado")); m.put("idUsuario",rs.getInt("id_usuario")); m.put("codigo",rs.getString("codigo_empleado")); m.put("fechaIngreso",rs.getDate("fecha_ingreso")); m.put("estadoLaboral",rs.getString("estado_laboral")); m.put("nombres",rs.getString("nombres")); m.put("apellidos",rs.getString("apellidos")); m.put("correo",rs.getString("correo")); m.put("telefono",rs.getString("telefono")); m.put("estadoUsuario",rs.getString("estado_usuario")); out.add(m); }
        } return out;
    }

    public boolean updateEmployee(int idEmpleado, String nombres, String apellidos, String correo, String telefono, String codigo, String estadoLaboral, String estadoUsuario) throws SQLException {
        String sql="UPDATE usuario u SET nombres=?, apellidos=?, correo=?, telefono=?, estado=? FROM empleado e WHERE e.id_empleado=? AND e.id_usuario=u.id_usuario";
        try(Connection c=ConnectionPostgresSQL.getInstancia().getConexion()) { c.setAutoCommit(false); try {
            try(PreparedStatement ps=c.prepareStatement(sql)){ ps.setString(1,nombres);ps.setString(2,apellidos);ps.setString(3,correo);ps.setString(4,telefono);ps.setString(5,estadoUsuario);ps.setInt(6,idEmpleado);ps.executeUpdate(); }
            try(PreparedStatement ps=c.prepareStatement("UPDATE empleado SET codigo_empleado=?, estado_laboral=? WHERE id_empleado=?")){ps.setString(1,codigo);ps.setString(2,estadoLaboral);ps.setInt(3,idEmpleado);ps.executeUpdate();}
            c.commit(); return true;
        } catch(SQLException ex){c.rollback();throw ex;} }
    }

    public boolean deleteEmployee(int idEmpleado) throws SQLException {
        String sql="UPDATE usuario SET estado='inactivo' WHERE id_usuario=(SELECT id_usuario FROM empleado WHERE id_empleado=?)";
        try(Connection c=ConnectionPostgresSQL.getInstancia().getConexion();PreparedStatement ps=c.prepareStatement(sql)){ps.setInt(1,idEmpleado);return ps.executeUpdate()>0;}
    }

    public java.util.Map<String,Object> getEmployee(int idEmpleado) throws SQLException {
        for(Map<String,Object> m:listEmployees()) if(((Number)m.get("idEmpleado")).intValue()==idEmpleado) return m;
        return null;
    }

    public List<ClienteDTO> listarClientes() {
        List<ClienteDTO> lista = new ArrayList<>();
        // Unimos con usuario_rol y rol para filtrar solo los que tengan rol 'CLIENTE'
        String sql = "SELECT u.id_usuario, u.nombres, u.apellidos, u.correo, u.telefono, u.estado, u.fecha_registro, u.ultimo_acceso " +
                "FROM usuario u " +
                "INNER JOIN usuario_rol ur ON u.id_usuario = ur.id_usuario " +
                "INNER JOIN rol r ON ur.id_rol = r.id_rol " +
                "WHERE r.nombre = 'CLIENTE' " +
                "ORDER BY u.id_usuario DESC";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ClienteDTO c = new ClienteDTO();
                c.setIdUsuario(rs.getInt("id_usuario"));
                c.setNombres(rs.getString("nombres"));
                c.setApellidos(rs.getString("apellidos"));
                c.setCorreo(rs.getString("correo"));
                c.setTelefono(rs.getString("telefono"));
                c.setEstado(rs.getString("estado"));
                c.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                c.setUltimoAcceso(rs.getTimestamp("ultimo_acceso"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean editarCliente(int idUsuario, String nombres, String apellidos, String correo, String telefono, String estado) {
        String sql = "UPDATE usuario SET nombres = ?, apellidos = ?, correo = ?, telefono = ?, estado = ? WHERE id_usuario = ?";
        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombres);
            ps.setString(2, apellidos);
            ps.setString(3, correo);
            ps.setString(4, telefono);
            ps.setString(5, estado);
            ps.setInt(6, idUsuario);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean bloquearCliente(int idUsuario) {
        String sql = "UPDATE usuario SET estado = 'bloqueado' WHERE id_usuario = ?";
        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
