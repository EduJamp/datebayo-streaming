package com.utp.model.dto;

public class CuponDTO {
    private int idCupon;
    private String codigo;
    private String nombreCampania;
    private double porcentajeDescuento;
    private Integer limiteUsos;
    private String estado;

    public CuponDTO() {
    }

    public int getIdCupon() {
        return idCupon;
    }

    public void setIdCupon(int idCupon) {
        this.idCupon = idCupon;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombreCampania() {
        return nombreCampania;
    }

    public void setNombreCampania(String nombreCampania) {
        this.nombreCampania = nombreCampania;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Integer getLimiteUsos() {
        return limiteUsos;
    }

    public void setLimiteUsos(Integer limiteUsos) {
        this.limiteUsos = limiteUsos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
