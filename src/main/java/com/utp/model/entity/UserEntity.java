package com.utp.model.entity;

import com.utp.util.enums.UserStatus;

import java.security.Timestamp;
import java.time.LocalDateTime;

public class UserEntity {
    private int idUser;
    private String name;
    private String lastname;
    private String email;
    private String passwordHash;
    private String cellphone;
    private UserStatus userStatus;
    private LocalDateTime registrationDate;
    private LocalDateTime lastAccessDate;

    public UserEntity() {
    }

    public UserEntity(int idUser, String name, String lastname, String email, String passwordHash,
                      String cellphone, UserStatus userStatus, LocalDateTime registrationDate, LocalDateTime lastAccessDate) {
        this.idUser = idUser;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.passwordHash = passwordHash;
        this.cellphone = cellphone;
        this.userStatus = userStatus;
        this.registrationDate = registrationDate;
        this.lastAccessDate = lastAccessDate;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(LocalDateTime lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }
}
