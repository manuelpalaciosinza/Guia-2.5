package Repositories;

import Entities.UsuarioEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class UsuarioRepository implements IRepository<UsuarioEntity>{

   private UsuarioRepository instance;
    private UsuarioRepository(){}
    public UsuarioRepository getInstanceOf(){
        if(instance == null){
            instance = new UsuarioRepository();
        }
        return instance;
    }

    @Override
    public void save(UsuarioEntity usuarioEntity) throws SQLException {

    }

    @Override
    public void deleteById(Integer id) throws SQLException {

    }

    @Override
    public ArrayList<UsuarioEntity> findAll() {
        return null;
    }

    @Override
    public Optional<UsuarioEntity> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Integer count() {
        return 0;
    }
}
