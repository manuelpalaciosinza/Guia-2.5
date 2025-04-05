package Repositories.impl;

import Entities.Classes.UsuarioEntity;
import Repositories.interfaces.IRepository;
import Repositories.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class UsuarioRepository implements IRepository<UsuarioEntity> {

   private static UsuarioRepository instance;
   private UsuarioRepository(){}
    public static UsuarioRepository getInstanceOf(){
        if(instance == null){
            instance = new UsuarioRepository();
        }
        return instance;
    }

    @Override
    public void save(UsuarioEntity usuarioEntity) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre,apellido,dni,email) VALUES(?,?,?,?)";
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,usuarioEntity.getNombre());
            ps.setString(2,usuarioEntity.getApellido());
            ps.setInt(3,usuarioEntity.getDni());
            ps.setString(4,usuarioEntity.getEmail());
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    usuarioEntity.setId_usuario(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try(Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1,id);
            ps.executeUpdate();
        }
    }
    public Optional<UsuarioEntity> resultToUsuario (ResultSet rs) throws SQLException{
       Optional<UsuarioEntity> usuarioEntity = Optional.of(new UsuarioEntity());
       usuarioEntity.get().setId_usuario(rs.getInt("id_usuario"));
       usuarioEntity.get().setNombre(rs.getString("nombre"));
       usuarioEntity.get().setApellido(rs.getString("apellido"));
       usuarioEntity.get().setDni(rs.getInt("dni"));
       usuarioEntity.get().setEmail(rs.getString("email"));
       usuarioEntity.get().setFecha_creacion(rs.getDate("fecha_creacion").toLocalDate());
       return usuarioEntity;
    }

    @Override
    public ArrayList<UsuarioEntity> findAll()throws SQLException {
        String sql = "SELECT * FROM usuarios";
        ArrayList <UsuarioEntity> listaUsuarios = new ArrayList<>();
        try (Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            try(ResultSet resultSet = ps.executeQuery();){
                while(resultSet.next()){
                    Optional<UsuarioEntity> usuarioEntity = resultToUsuario(resultSet);
                    usuarioEntity.ifPresent(listaUsuarios::add);
                }
            }
        }
        return listaUsuarios;
    }

    @Override
    public Optional<UsuarioEntity> findById(Integer id) throws SQLException {
       String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
       try (Connection connection = SQLiteConnection.getConnection();
       PreparedStatement ps = connection.prepareStatement(sql)){
           ps.setInt(1,id);
           try (ResultSet rs = ps.executeQuery()){
               if (rs.next()){
                 return resultToUsuario(rs);
               }
           }
       }
       return Optional.empty();
    }

    @Override
    public Integer count() throws SQLException{
        String sql = "SELECT COUNT(*) FROM usuarios";
        try(Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public void update(UsuarioEntity usuarioEntity) throws SQLException {
       String sql = "UPDATE usuarios SET nombre = ?,apellido = ?,dni = ?,email = ?,fecha_creacion = ? WHERE id_usuario = ? ";
       try(Connection connection = SQLiteConnection.getConnection();
       PreparedStatement ps = connection.prepareStatement(sql)){
           ps.setString(1,usuarioEntity.getNombre());
           ps.setString(2,usuarioEntity.getApellido());
           ps.setInt(3,usuarioEntity.getDni());
           ps.setString(4,usuarioEntity.getEmail());
           ps.setDate(5, Date.valueOf(usuarioEntity.getFecha_creacion()));
           ps.setInt(6,usuarioEntity.getId_usuario());
           ps.executeUpdate();
       }
    }
}
