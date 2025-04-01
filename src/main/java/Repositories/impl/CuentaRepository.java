package Repositories.impl;

import Entities.Classes.CuentaEntity;
import Entities.Enum.ETipo;
import Repositories.SQLiteConnection;
import Repositories.interfaces.IRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CuentaRepository implements IRepository<CuentaEntity> {

    private static CuentaRepository instance;
    private CuentaRepository(){}
    public static CuentaRepository getInstanceOf(){
        if(instance == null)
        {
            instance = new CuentaRepository();
        }
        return instance;
    }

    private Optional<CuentaEntity> resultToCuenta (ResultSet rs) throws SQLException{
        Optional<CuentaEntity> cuentaEntity = Optional.of(new CuentaEntity());
        cuentaEntity.get().setId_cuenta(rs.getInt("id_cuenta"));
        cuentaEntity.get().setId_usuario(rs.getInt("id_usuario"));
        cuentaEntity.get().setTipo_cuenta(ETipo.valueOf(rs.getString("tipo")));
        cuentaEntity.get().setSaldo(rs.getFloat("saldo"));
        cuentaEntity.get().setFecha_creacion(rs.getDate("fecha_creacion").toLocalDate());
        return cuentaEntity;
    }

    @Override
    public Integer count() throws SQLException{
        String sql = "SELECT COUNT(*) FROM cuentas";
        try(Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public Optional<CuentaEntity> findById(Integer id) throws SQLException{
        String sql = "SELECT * FROM cuentas WHERE id_cuenta = ?";
        try (Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    return resultToCuenta(rs);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public ArrayList<CuentaEntity> findAll() throws SQLException {
        ArrayList<CuentaEntity> list = new ArrayList<>();
        Optional<CuentaEntity> cuentaEntity = Optional.of(new CuentaEntity());
        String sql = "SELECT * FROM cuentas";
        try (Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    cuentaEntity = resultToCuenta(rs);
                    cuentaEntity.ifPresent(list::add);
                }
            }
        }
        return list;
    }
    public ArrayList<CuentaEntity> findAllByIdUser(int userId) throws SQLException {
        ArrayList<CuentaEntity> list = new ArrayList<>();
        Optional<CuentaEntity> cuentaEntity = Optional.of(new CuentaEntity());
        String sql = "SELECT * FROM cuentas WHERE id_usuario = ?";
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,userId);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    cuentaEntity = resultToCuenta(rs);
                    cuentaEntity.ifPresent(list::add);
                }
            }
        }
        return list;
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM cuentas WHERE id_cuenta = ?";
        try (Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,id);
            ps.executeUpdate();
        }
    }

    @Override
    public void save(CuentaEntity cuentaEntity) throws SQLException {
        String sql = "INSERT INTO cuentas (id_usuario, tipo, saldo) VALUES(?,?,?)";
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,cuentaEntity.getId_usuario());
            ps.setString(2,cuentaEntity.getTipo_cuenta().name());
            ps.setFloat(3,cuentaEntity.getSaldo());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(CuentaEntity cuentaEntity) throws SQLException {
        String sql = "UPDATE cuentas SET id_usuario = ?,tipo = ?, saldo = ?, fecha_creacion = ? WHERE id_cuenta  = ?";
        try (Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,cuentaEntity.getId_usuario());
            ps.setString(2,cuentaEntity.getTipo_cuenta().name());
            ps.setFloat(3,cuentaEntity.getSaldo());
            ps.setDate(4, Date.valueOf(cuentaEntity.getFecha_creacion()));
            ps.setInt(5,cuentaEntity.getId_cuenta());
            ps.executeUpdate();
        }
    }
}
