package Entities;

public enum EPermiso {
    CLIENTE("CLIENTE"),
    ADMINISTRADOR("ADMINISTRADOR"),
    GESTOR("GESTOR");

    private String texto;

    EPermiso(String texto) {
        this.texto = texto;
    }
}
