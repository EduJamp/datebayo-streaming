package com.utp.model.dto;

public class CredentialDeliveredDTO {
    private String numeroVenta;
    private String nombreProducto;
    private String correoCuenta;
    private String nombrePerfil;
    private String pinOContrasenaPerfil;
    private String contrasenaCuenta;

    public CredentialDeliveredDTO(String numeroVenta, String nombreProducto, String correoCuenta, String contrasenaCuenta, String pinOContrasenaPerfil, String nombrePerfil) {
        this.numeroVenta = numeroVenta;
        this.nombreProducto = nombreProducto;
        this.correoCuenta = correoCuenta;
        this.contrasenaCuenta = contrasenaCuenta;
        this.nombrePerfil = nombrePerfil;
        this.pinOContrasenaPerfil = pinOContrasenaPerfil;
    }

    public String getNumeroVenta() {
        return numeroVenta;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getCorreoCuenta() {
        return correoCuenta;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public String getPinOContrasenaPerfil() {
        return pinOContrasenaPerfil;
    }

    public String getContrasenaCuenta() {
        return contrasenaCuenta;
    }

    public void setContrasenaCuenta(String contrasenaCuenta) {
        this.contrasenaCuenta = contrasenaCuenta;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setCorreoCuenta(String correoCuenta) {
        this.correoCuenta = correoCuenta;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    public void setPinOContrasenaPerfil(String pinOContrasenaPerfil) {
        this.pinOContrasenaPerfil = pinOContrasenaPerfil;
    }
}
