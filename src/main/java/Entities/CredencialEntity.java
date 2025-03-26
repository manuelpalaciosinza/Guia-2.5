package Entities;

public class CredencialEntity {

    private Integer id_credencial; ///PK
    private Integer id_usuario; ///FK
    private String username; ///UNQ
    private String password;
    private EPermiso permiso;

    public CredencialEntity(Integer id_credencial, Integer id_usuario, String username, String password, EPermiso permiso) {
        this.id_credencial = id_credencial;
        this.id_usuario = id_usuario;
        this.username = username;
        this.password = password;
        this.permiso = permiso;
    }

    public CredencialEntity(Integer id_usuario, String username, EPermiso permiso) {
        this.id_usuario = id_usuario;
        this.username = username;
        this.password = "1234"; ///Se genera automaticamente como 1234 cada vez que se crea una cuenta
        this.permiso = permiso;
    }

    public CredencialEntity(){
        id_credencial = 0;
        id_usuario = 0;
        username = "";
        password = "";
        permiso = null;
    }

    public Integer getId_credencial() {
        return id_credencial;
    }

    public void setId_credencial(Integer id_credencial) {
        this.id_credencial = id_credencial;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EPermiso getPermiso() {
        return permiso;
    }

    public void setPermiso(EPermiso permiso) {
        this.permiso = permiso;
    }
}
