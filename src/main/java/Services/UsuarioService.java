package Services;

import Entities.Classes.CredencialEntity;
import Entities.Classes.CuentaEntity;
import Entities.Classes.UsuarioEntity;
import Entities.Enum.EPermiso;
import Entities.Enum.ETipo;
import Exceptions.NoAutorizadoException;
import Repositories.impl.CredencialRepository;
import Repositories.impl.CuentaRepository;
import Repositories.impl.UsuarioRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UsuarioService {

    /// Services no retornan optional
    /// Gestionar permisos del usuario en la clase service

    private final UsuarioRepository usuarioRepository;
    private final CredencialRepository credencialRepository;
    private final CuentaRepository cuentaRepository;
    private static UsuarioService instance;

    private UsuarioService() {
        usuarioRepository = UsuarioRepository.getInstanceOf();
        cuentaRepository = CuentaRepository.getInstanceOf();
        credencialRepository = CredencialRepository.getInstanceOf();
    }

    public static UsuarioService getInstanceOf() {
        if (instance == null) {
            instance = new UsuarioService();
        }
        return instance;
    }

    public void registrarUsuario(String nombre, String apellido, int dni, String email) {

        try {
            UsuarioEntity aRegistrar = new UsuarioEntity(nombre, apellido, dni, email);
            usuarioRepository.save(aRegistrar);
            CredencialEntity credencial = new CredencialEntity(aRegistrar.getId_usuario(), generarUsername(email));
            credencialRepository.save(credencial);
            CuentaEntity cuenta = new CuentaEntity(aRegistrar.getId_usuario(), ETipo.CAJA_AHORRO, 0.0f);
            cuentaRepository.save(cuenta);
            System.out.println("Usuario registrado con exito");
        } catch (SQLException e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
        }
    }

    private String generarUsername(String email) {
        return email.split("@")[0];
    }

    public UsuarioEntity iniciarSesion(String username, String password) {
        /// Guardar el usuario totalmente cargado en el menu
        CredencialEntity credencial = new CredencialEntity();
        UsuarioEntity usuario = new UsuarioEntity();
        try {
            credencial = credencialRepository
                    .findByUsernameAndPass(username,password)
                    .orElseThrow(()->new NoSuchElementException("Usuario no encontrado"));
            usuario = usuarioRepository
                    .findById(credencial.getId_usuario())
                    .orElseThrow(()->new RuntimeException("Usuario no encontrado"));
            ArrayList<CuentaEntity> listaCuentas = new ArrayList<>();
            listaCuentas = cuentaRepository.
                    findAllByIdUser(usuario.getId_usuario());
            usuario.setCredencial(credencial);
            usuario.setCuentas(listaCuentas);
        }
        catch (SQLException e){
            System.out.println("Usuario o contraseña incorrectos");
        }
        return usuario;
    }

    public String listarTodosUsuarios(UsuarioEntity usuario) throws NoAutorizadoException{
        ArrayList<UsuarioEntity> listaUsuarios = new ArrayList<>();
        if(usuario.getCredencial().getPermiso().equals(EPermiso.ADMINISTRADOR) ||
        usuario.getCredencial().equals(EPermiso.GESTOR)) {
            try {
                listaUsuarios = usuarioRepository.findAll();
            } catch (SQLException e) {
                System.out.println("Error: No se encuentran usuarios en el sistema");
            }
            return listaUsuarios.toString();
        }
        else {
             throw new NoAutorizadoException("El usuario no cuenta con los permisos necesarios para realizar esta accion");
        }
    }
}
