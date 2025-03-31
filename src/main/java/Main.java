import Entities.Classes.CredencialEntity;
import Entities.Classes.CuentaEntity;
import Entities.Classes.UsuarioEntity;
import Entities.Enum.EPermiso;
import Entities.Enum.ETipo;
import Repositories.impl.CredencialRepository;
import Repositories.impl.CuentaRepository;
import Repositories.impl.UsuarioRepository;

import java.sql.SQLException;
import java.util.Optional;

public class Main {
    public static void main (String [] args){
       /* CuentaRepository cuentaRepository = CuentaRepository.getInstanceOf();
        CuentaEntity cuentaEntity = new CuentaEntity(5, ETipo.CAJA_AHORRO, 55.34F);
        try {
            Optional<CuentaEntity> cuentaEntity1 = cuentaRepository.findById(4);
            cuentaEntity1.get().setSaldo(69.69f);
            cuentaRepository.update(cuentaEntity1.orElse(new CuentaEntity()));
            System.out.println(cuentaRepository.findById(4));
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        */
        CredencialRepository credencialRepository = CredencialRepository.getInstanceOf();
        CredencialEntity credencial = new CredencialEntity(8,"Manupala", EPermiso.ADMINISTRADOR);
        try {
            credencialRepository.save(credencial);
            System.out.println(credencialRepository.findById(5));
            credencialRepository.deleteById(6);
            System.out.println(credencialRepository.findAll());
            System.out.println(credencialRepository.count());

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
