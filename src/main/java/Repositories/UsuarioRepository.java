package Repositories;

import Connections.SQLiteConnection;
import Entities.UsuarioEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class UsuarioRepository implements IRepository<UsuarioEntity>{

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
        String sql = "INSERT INTO usuarios (nombre,apellido,dni,email,fecha_creacion) VALUES(?,?,?,?,?)";
        try (Connection connection = SQLiteConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,usuarioEntity.getNombre());
            ps.setString(2,usuarioEntity.getApellido());
            ps.setInt(3,usuarioEntity.getDni());
            ps.setString(4,usuarioEntity.getEmail());
            ps.setDate(5, Date.valueOf(usuarioEntity.getFecha_creacion())); ///Revisar
            ps.executeUpdate();
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

    @Override
    public ArrayList<UsuarioEntity> findAll()throws SQLException {
        String sql = "SELECT * FROM usuarios";
        ArrayList <UsuarioEntity> listaUsuarios = new ArrayList<>();
        try (Connection connection = SQLiteConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            try(ResultSet resultSet = ps.executeQuery();){
                while(resultSet.next()){
                    /// resultToUsuario();

                }

            }
        }
        return listaUsuarios;
    }

    @Override
    public Optional<UsuarioEntity> findById(Integer id) throws SQLException {

       String sql = "SELECT * FROM alumnos WHERE id_alumno = ?";
       try (Connection connection = SQLiteConnection.getConnection();
       PreparedStatement ps = connection.prepareStatement(sql)){
           ps.setInt(1,id);
           try (ResultSet rs = ps.executeQuery()){
               if (rs.next()){
                 ///  return Optional.of(ResultToUsuario);
               }
           }

       }
        return Optional.empty();
    }

    @Override
    public Integer count() {
        return 0;
    }

    @Override
    public void update(int id) throws SQLException {

    }
}
