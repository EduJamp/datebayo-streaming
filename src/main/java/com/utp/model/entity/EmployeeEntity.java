package com.utp.model.entity;

import com.utp.util.enums.EmployementStatus;

import java.util.Date;

public class EmployeeEntity {
    private int idEmployee;
    private int idUser;
    private String employeeCode;
    private Date dateOfEntry;
    private EmployementStatus employementStatus;
    private Integer createdBy;

    public EmployeeEntity() {
    }

    public EmployeeEntity(int idEmployee, int idUser, String employeeCode, Date dateOfEntry,
                          EmployementStatus employementStatus, Integer createdBy) {
        this.idEmployee = idEmployee;
        this.idUser = idUser;
        this.employeeCode = employeeCode;
        this.dateOfEntry = dateOfEntry;
        this.employementStatus = employementStatus;
        this.createdBy = createdBy;
    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public Date getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(Date dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public EmployementStatus getEmployementStatus() {
        return employementStatus;
    }

    public void setEmployementStatus(EmployementStatus employementStatus) {
        this.employementStatus = employementStatus;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
}
