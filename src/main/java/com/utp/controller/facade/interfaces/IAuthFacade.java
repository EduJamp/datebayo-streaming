package com.utp.controller.facade.interfaces;

import com.utp.model.dto.UserSesionDTO;
import com.utp.model.entity.EmployeeEntity;
import com.utp.model.entity.UserEntity;

public interface IAuthFacade {
    boolean registerClient(UserEntity user);
    UserSesionDTO login(String email, String password);
    boolean registerEmployee(UserEntity user, EmployeeEntity employee, int idAdminCreator);
}
