package Services;

import Entities.Classes.CredencialEntity;
import Entities.Classes.CuentaEntity;
import Entities.Classes.UsuarioEntity;
import Entities.Enum.EPermiso;
import Exceptions.NoAutorizadoException;
import Repositories.impl.CredencialRepository;
import Repositories.impl.UsuarioRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CredencialService {

    private final CredencialRepository credencialRepository;
    private static CredencialService instance;

    private CredencialService(){
        credencialRepository = CredencialRepository.getInstanceOf();
    }

    public static CredencialService getInstanceOf (){
        if (instance == null){
            instance = new CredencialService();
        }
        return instance;
    }

    public Map<String, Long> obtenerUsuariosPorPermiso(UsuarioEntity usuarioLogueado) throws NoAutorizadoException {
        Map<String, Long> resultado = new HashMap<>();
        if (usuarioLogueado.getCredencial().getPermiso().equals(EPermiso.CLIENTE)) {
            throw new NoAutorizadoException("El usuario no cuenta con los permisos para realizar esta accion");
        }
        try {
            ArrayList<CredencialEntity> listaCredenciales = credencialRepository.findAll();
            resultado = listaCredenciales.stream()
                    .collect(Collectors.groupingBy(credencial -> credencial.getPermiso().name(), Collectors.counting()));

        } catch (SQLException e) {
            System.out.println("Error en la conexion con la base de datos: " + e.getMessage());
        }
        return resultado;
    }

}
