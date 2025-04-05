import Entities.Classes.CredencialEntity;
import Entities.Classes.CuentaEntity;
import Entities.Classes.UsuarioEntity;
import Entities.Enum.EPermiso;
import Entities.Enum.ETipo;
import Repositories.impl.CredencialRepository;
import Repositories.impl.CuentaRepository;
import Repositories.impl.UsuarioRepository;
import Services.UsuarioService;
import UI.App;

import java.sql.SQLException;
import java.util.Optional;

public class Main {
    public static void main (String [] args){

        App aplicacion = new App();
        aplicacion.iniciarApp();
    }
}
