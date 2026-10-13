package com.utp.model.entity;

public class RolEntity {
    private int idRol;
    private String rolName;
    private String rolDescription;

    public RolEntity() {
    }

    public RolEntity(int idRol, String rolName, String rolDescription) {
        this.idRol = idRol;
        this.rolName = rolName;
        this.rolDescription = rolDescription;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getRolName() {
        return rolName;
    }

    public void setRolName(String rolName) {
        this.rolName = rolName;
    }

    public String getRolDescription() {
        return rolDescription;
    }

    public void setRolDescription(String rolDescription) {
        this.rolDescription = rolDescription;
    }
}
