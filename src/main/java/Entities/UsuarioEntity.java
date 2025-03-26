package Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class UsuarioEntity {

    private Integer id_usuario; ///PK
    private String nombre;
    private String apellido;
    private Integer dni; ///UNQ
    private String email; ///UNQ
    private LocalDateTime fechaCreacion;
    private ArrayList<CuentaEntity> cuentas;
    private CredencialEntity credencial;

    public UsuarioEntity(Integer id_usuario, String nombre, String apellido, Integer dni, String email, LocalDateTime fechaCreacion,CredencialEntity credencial) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fechaCreacion = fechaCreacion;
        this.cuentas = new ArrayList<CuentaEntity>();
        this.credencial = credencial;
    }
    public UsuarioEntity (String nombre, String apellido, Integer dni,String email,CredencialEntity credencial){
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fechaCreacion = LocalDateTime.now();
        this.cuentas = new ArrayList<CuentaEntity>();
        this.credencial = credencial;
    }
    public UsuarioEntity(){
        id_usuario = 0;
        nombre = "";
        apellido = "";
        dni = 0;
        email = "";
        fechaCreacion = null;
        cuentas = new ArrayList<CuentaEntity>();
        credencial = null;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public ArrayList<CuentaEntity> getCuentas() {
        return cuentas;
    }

    public void setCuentas(ArrayList<CuentaEntity> cuentas) {
        this.cuentas = cuentas;
    }

    public CredencialEntity getCredencial() {
        return credencial;
    }

    public void setCredencial(CredencialEntity credencial) {
        this.credencial = credencial;
    }

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "id_usuario=" + id_usuario +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni=" + dni +
                ", email='" + email + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", cuentas=" + cuentas +
                ", credencial=" + credencial +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioEntity that = (UsuarioEntity) o;
        return Objects.equals(id_usuario, that.id_usuario) && Objects.equals(dni, that.dni) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_usuario, dni, email);
    }
}
