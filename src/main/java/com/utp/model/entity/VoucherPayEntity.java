package com.utp.model.entity;

import java.security.Timestamp;

//Comprobante de pago
public class VoucherPayEntity {
    private int idVoucherPay;
    private int idPay;
    private String fileUrl;
    private String fileName;
    private String mimeType; //es un estándar digital que identifica el formato de un archivo
    private Timestamp uploadDate;
    private String observationReview;

    public VoucherPayEntity() {
    }

    public VoucherPayEntity(int idVoucherPay, int idPay, String fileUrl, String fileName, String mimeType,
                            Timestamp uploadDate, String observationReview) {
        this.idVoucherPay = idVoucherPay;
        this.idPay = idPay;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.uploadDate = uploadDate;
        this.observationReview = observationReview;
    }

    public int getIdVoucherPay() {
        return idVoucherPay;
    }

    public void setIdVoucherPay(int idVoucherPay) {
        this.idVoucherPay = idVoucherPay;
    }

    public int getIdPay() {
        return idPay;
    }

    public void setIdPay(int idPay) {
        this.idPay = idPay;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getObservationReview() {
        return observationReview;
    }

    public void setObservationReview(String observationReview) {
        this.observationReview = observationReview;
    }
}
