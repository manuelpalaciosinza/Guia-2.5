package Repositories;

import Entities.CuentaEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class CuentaRepository implements IRepository<CuentaEntity> {

    private CuentaRepository instance;
    private CuentaRepository(){}
    public CuentaRepository getInstanceOf(){
        if(instance == null)
        {
            instance = new CuentaRepository();
        }
        return instance;
    }

    @Override
    public Integer count() {
        return 0;
    }

    @Override
    public Optional<CuentaEntity> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public ArrayList<CuentaEntity> findAll() {
        return null;
    }

    @Override
    public void deleteById(Integer id) throws SQLException {

    }

    @Override
    public void save(CuentaEntity cuentaEntity) throws SQLException {

    }
}
