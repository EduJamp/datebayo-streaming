package com.utp.controller.facade;

import com.utp.controller.facade.interfaces.IAuthFacade;
import com.utp.model.dto.UserSesionDTO;
import com.utp.model.entity.EmployeeEntity;
import com.utp.model.entity.UserEntity;
import com.utp.persistence.dao.UserDAO;

import java.sql.SQLException;

public class AuthFacade implements IAuthFacade {
    private final UserDAO userDAO = new UserDAO();

    @Override
    public boolean registerClient(UserEntity user) {
        try {
            UserEntity existente = userDAO.findByEmail(user.getEmail());

            if (existente != null) {
                return false;
            }

            return userDAO.registerClient(user);

        } catch( SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserSesionDTO login(String email, String password) {
        try {
            return userDAO.login(email, password);
        } catch( SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean registerEmployee(UserEntity user, EmployeeEntity employee, int idAdminCreator) {
        try {
            return userDAO.registerEmployee(user, employee, idAdminCreator);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
