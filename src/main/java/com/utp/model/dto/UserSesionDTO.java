package com.utp.model.dto;

import java.util.List;

public class UserSesionDTO {

    private final int idUser;
    private final String nameUser;
    private final String emailUser;
    private final List<String> rolesUser;

    public UserSesionDTO(
            int idUser,
            String nameUser,
            String emailUser,
            List<String> rolesUser) {
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.emailUser = emailUser;
        this.rolesUser = rolesUser;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public List<String> getRolesUser() {
        return rolesUser;
    }
}
