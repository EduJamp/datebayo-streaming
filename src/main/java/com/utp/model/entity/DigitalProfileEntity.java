package com.utp.model.entity;

import com.utp.util.enums.DigitalProfileStatus;

import java.security.Timestamp;

public class DigitalProfileEntity {
    private int idDigitalProfile;
    private int idAcountDigital;
    private String profileName;
    private String profileUser;
    private String encryptedPasswordProfile;
    private DigitalProfileStatus digitalProfileStatus;
    private Timestamp assigmentDate;

    public DigitalProfileEntity() {
    }

    public DigitalProfileEntity(int idDigitalProfile, int idAcountDigital, String profileName, String profileUser,
                                String encryptedPasswordProfile, DigitalProfileStatus digitalProfileStatus,
                                Timestamp assigmentDate) {
        this.idDigitalProfile = idDigitalProfile;
        this.idAcountDigital = idAcountDigital;
        this.profileName = profileName;
        this.profileUser = profileUser;
        this.encryptedPasswordProfile = encryptedPasswordProfile;
        this.digitalProfileStatus = digitalProfileStatus;
        this.assigmentDate = assigmentDate;
    }

    public int getIdDigitalProfile() {
        return idDigitalProfile;
    }

    public void setIdDigitalProfile(int idDigitalProfile) {
        this.idDigitalProfile = idDigitalProfile;
    }

    public int getIdAcountDigital() {
        return idAcountDigital;
    }

    public void setIdAcountDigital(int idAcountDigital) {
        this.idAcountDigital = idAcountDigital;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileUser() {
        return profileUser;
    }

    public void setProfileUser(String profileUser) {
        this.profileUser = profileUser;
    }

    public String getEncryptedPasswordProfile() {
        return encryptedPasswordProfile;
    }

    public void setEncryptedPasswordProfile(String encryptedPasswordProfile) {
        this.encryptedPasswordProfile = encryptedPasswordProfile;
    }

    public DigitalProfileStatus getDigitalProfileStatus() {
        return digitalProfileStatus;
    }

    public void setDigitalProfileStatus(DigitalProfileStatus digitalProfileStatus) {
        this.digitalProfileStatus = digitalProfileStatus;
    }

    public Timestamp getAssigmentDate() {
        return assigmentDate;
    }

    public void setAssigmentDate(Timestamp assigmentDate) {
        this.assigmentDate = assigmentDate;
    }
}
