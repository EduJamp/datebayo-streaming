package com.utp.util.enums;

public enum PayStatus {
    pendiente,
    en_revision,
    aprobado,
    rechazado,
    reembolsado;

    public static PayStatus fromString(String text) {
        if (text == null) {
            return null;
        }
        for (PayStatus status : PayStatus.values()) {
            if (status.name().equalsIgnoreCase(text)) {
                return status;
            }
        }
        return en_revision;
    }
}
