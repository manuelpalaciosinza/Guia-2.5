package Repositories.impl;

import Entities.Classes.CredencialEntity;
import Entities.Classes.CuentaEntity;
import Entities.Enum.EPermiso;
import Repositories.SQLiteConnection;
import Repositories.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CredencialRepository implements IRepository<CredencialEntity> {
    private static CredencialRepository instance;
    private CredencialRepository(){}
    public static CredencialRepository getInstanceOf(){
        if (instance == null){
            instance = new CredencialRepository();
        }
        return instance;
    }

    private Optional<CredencialEntity> resultToCredencial(ResultSet rs) throws SQLException{
        Optional<CredencialEntity> credencial = Optional.of(new CredencialEntity());
        credencial.get().setId_credencial(rs.getInt("id_credencial"));
        credencial.get().setId_usuario(rs.getInt("id_usuario"));
        credencial.get().setUsername(rs.getString("username"));
        credencial.get().setPassword(rs.getString("password"));
        credencial.get().setPermiso(EPermiso.valueOf(rs.getString("permiso")));
        return credencial;
    }
    @Override
    public void save(CredencialEntity credencial) throws SQLException {
        String sql = "INSERT INTO credenciales (id_usuario,username,password,permiso) VALUES(?,?,?,?)";
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,credencial.getId_usuario());
            ps.setString(2, credencial.getUsername());
            ps.setString(3, credencial.getPassword());
            ps.setString(4,credencial.getPermiso().name());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM credenciales WHERE id_credencial = ?";
        try (Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,id);
            ps.executeUpdate();
        }
    }public void deleteByIdUser (Integer idUser) throws SQLException {
        String sql = "DELETE FROM credenciales WHERE id_usuario = ?";
        try (Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,idUser);
            ps.executeUpdate();
        }
    }

    @Override
    public ArrayList<CredencialEntity> findAll() throws SQLException{
        ArrayList<CredencialEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM credenciales";
        try (Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    Optional<CredencialEntity> credencial = resultToCredencial(rs);
                    credencial.ifPresent(list::add);
                }
            }
        }
        return list;
    }

    @Override
    public Optional<CredencialEntity> findById(Integer id) throws SQLException{
        String sql = "SELECT * FROM credenciales WHERE id_credencial = ?";
        try (Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    return resultToCredencial(rs);
                }
            }
        }
        return Optional.empty();
    }
    public Optional<CredencialEntity> findByIdUser(Integer idUser) throws SQLException{
        String sql = "SELECT * FROM credenciales WHERE id_usuario = ?";
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,idUser);
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    return resultToCredencial(rs);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Integer count() throws  SQLException{
        String sql = "SELECT COUNT (*) FROM credenciales";
        try (Connection connection = SQLiteConnection.getConnection();
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
    public void update(CredencialEntity credencialEntity) throws SQLException {
        String sql = "UPDATE credenciales SET id_usuario = ?,username= ?, password = ?, permiso = ? WHERE id_credencial = ?";
        try (Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,credencialEntity.getId_usuario());
            ps.setString(2,credencialEntity.getUsername());
            ps.setString(3,credencialEntity.getPassword());
            ps.setString(4,credencialEntity.getPermiso().name());
            ps.setInt(5,credencialEntity.getId_credencial());
            ps.executeUpdate();
        }
    }
    public Optional<CredencialEntity> findByUsernameAndPass (String username,String password) throws SQLException{
        String sql = "SELECT * FROM credenciales WHERE username = ? AND password = ? ";
        try (Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,username);
            ps.setString(2,password);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return resultToCredencial(rs);
                }
            }
        }
        return Optional.empty();
    }
}

