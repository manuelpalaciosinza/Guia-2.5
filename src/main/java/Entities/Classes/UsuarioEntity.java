package Entities.Classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class UsuarioEntity {

    private Integer id_usuario; ///PK
    private String nombre;
    private String apellido;
    private Integer dni; ///UNQ
    private String email; ///UNQ
    private LocalDate fecha_creacion;
    private CredencialEntity credencial;
    private ArrayList<CuentaEntity> cuentas;

    public UsuarioEntity(Integer id_usuario, String nombre, String apellido, Integer dni, String email, LocalDate fechaCreacion,CredencialEntity credencial) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fecha_creacion = fechaCreacion;
        this.cuentas = new ArrayList<CuentaEntity>();
        this.credencial = credencial;
    }
    public UsuarioEntity (String nombre, String apellido, Integer dni,String email){
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.fecha_creacion = null;
        this.cuentas = new ArrayList<CuentaEntity>();
        this.credencial = null;
    }
    public UsuarioEntity(){
        id_usuario = 0;
        nombre = "";
        apellido = "";
        dni = 0;
        email = "";
        fecha_creacion = null;
        cuentas = new ArrayList<CuentaEntity>();
        credencial = null;
    }
    public UsuarioEntity(String nombre, String apellido, String email){
        id_usuario = 0;
        this.nombre = nombre;
        this.apellido = apellido;
        dni = 0;
        this.email = email;
        fecha_creacion = null;
        cuentas = null;
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

    public LocalDate getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDate fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
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
        return "\nUsuarioEntity{" +
                "id_usuario=" + id_usuario +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni=" + dni +
                ", email='" + email + '\'' +
                ", fechaCreacion=" + fecha_creacion +
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
