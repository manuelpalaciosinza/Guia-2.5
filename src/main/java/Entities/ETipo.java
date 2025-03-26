package Entities;

public enum ETipo {
    CAJA_AHORRO("CAJA_AHORRO"),
    CUENTA_CORRIENTE("CUENTA_CORRIENTE");

    private String texto;

    ETipo(String texto) {
        this.texto = texto;
    }
}
