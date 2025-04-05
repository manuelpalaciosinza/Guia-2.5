package Entities.Classes;

import Entities.Enum.ETipo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CuentaEntity {
    private Integer id_cuenta; ///PK
    private Integer id_usuario; ///FK
    private ETipo tipo_cuenta;
    private Float saldo;
    private LocalDate fecha_creacion;

    public CuentaEntity(Integer id_cuenta, Integer id_usuario, ETipo tipo_cuenta, Float saldo, LocalDate fecha_creacion) {
        this.id_cuenta = id_cuenta;
        this.id_usuario = id_usuario;
        this.tipo_cuenta = tipo_cuenta;
        this.saldo = saldo;
        this.fecha_creacion = fecha_creacion;
    }

    public CuentaEntity(Integer id_usuario, ETipo tipo_cuenta, Float saldo) {
        this.id_usuario = id_usuario;
        this.tipo_cuenta = tipo_cuenta;
        this.saldo = saldo;
        fecha_creacion = null;
    }

    public CuentaEntity(Integer id_usuario, Float saldo) {
        this.id_usuario = id_usuario;
        this.tipo_cuenta = ETipo.CUENTA_CORRIENTE;
        this.saldo = saldo;
        fecha_creacion = null;
    }
    public CuentaEntity(){
        id_cuenta = 0;
        id_usuario = 0;
        tipo_cuenta = null;
        saldo = 0.0F;
        fecha_creacion = null;
    }

    public Integer getId_cuenta() {
        return id_cuenta;
    }

    public void setId_cuenta(Integer id_cuenta) {
        this.id_cuenta = id_cuenta;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public ETipo getTipo_cuenta() {
        return tipo_cuenta;
    }

    public void setTipo_cuenta(ETipo tipo_cuenta) {
        this.tipo_cuenta = tipo_cuenta;
    }

    public Float getSaldo() {
        return saldo;
    }

    public void setSaldo(Float saldo) {
        this.saldo = saldo;
    }

    public LocalDate getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDate fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    @Override
    public String toString() {
        return "CuentaEntity{" +
                "id_cuenta=" + id_cuenta +
                ", id_usuario=" + id_usuario +
                ", tipo_cuenta=" + tipo_cuenta +
                ", saldo=" + saldo +
                ", fecha_creacion=" + fecha_creacion +
                '}';
    }
}
