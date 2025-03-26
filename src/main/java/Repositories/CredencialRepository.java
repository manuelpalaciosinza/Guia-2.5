package Repositories;

import Entities.CredencialEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class CredencialRepository implements IRepository<CredencialEntity>{
    private CredencialRepository instance;
    private CredencialRepository(){}
    public CredencialRepository getInstanceOf(){
        if (instance == null){
            instance = new CredencialRepository();
        }
        return instance;
    }

    @Override
    public void save(CredencialEntity credencial) throws SQLException {

    }

    @Override
    public void deleteById(Integer id) throws SQLException {

    }

    @Override
    public ArrayList<CredencialEntity> findAll() {
        return null;
    }

    @Override
    public Optional<CredencialEntity> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Integer count() {
        return 0;
    }
}
