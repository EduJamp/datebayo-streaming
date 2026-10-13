package com.utp.util.enums;

public enum SalesStatus {
    pendiente_pago,
    pago_en_revision,
    pago_confirmado,
    en_proceso,
    parcialmente_entregada,
    entregada,
    cancelada,
    reembolsada;

    public static SalesStatus fromString(String text) {
        if (text == null) {
            return null;
        }
        for (SalesStatus status : SalesStatus.values()) {
            if (status.name().equalsIgnoreCase(text)) {
                return status;
            }
        }
        return cancelada;
    }
}
