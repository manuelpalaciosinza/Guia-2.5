import Entities.UsuarioEntity;
import Repositories.UsuarioRepository;

import java.sql.SQLException;

public class Main {
    public static void main (String [] args){
        UsuarioRepository usuarioRepository = UsuarioRepository.getInstanceOf();
        UsuarioEntity usuarioEntity = new UsuarioEntity("Panuel","Malacios",40946564,"manuelpalaciosinsta@gmail.com");
        try {
            usuarioRepository.deleteById(6);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
