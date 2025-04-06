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
import java.util.*;
import java.util.stream.Collectors;

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
            System.out.println("Usuario registrado con exito" +
                    "\nNombre de usuario: " + generarUsername(email) +
                    "\nContrasenia: 1234");
        } catch (SQLException e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
        }
    }

    private String generarUsername(String email) {
        return email.split("@")[0];
    }

    public UsuarioEntity iniciarSesion(String username, String password) {
        /// Guardar el usuario totalmente cargado en el menu
        CredencialEntity credencial;
        UsuarioEntity usuario = new UsuarioEntity();
        try {
            credencial = credencialRepository
                    .findByUsernameAndPass(username,password)
                    .orElseThrow(()->new NoSuchElementException("Usuario no encontrado"));
            usuario = usuarioRepository
                    .findById(credencial.getId_usuario())
                    .orElseThrow(()->new NoSuchElementException("Usuario no encontrado"));
            ArrayList<CuentaEntity> listaCuentas;
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

    public String listarTodosUsuarios(UsuarioEntity logueado) throws NoAutorizadoException{
        ArrayList<UsuarioEntity> listaUsuarios = new ArrayList<>();
        if(logueado.getCredencial().getPermiso().equals(EPermiso.ADMINISTRADOR) ||
        logueado.getCredencial().getPermiso().equals(EPermiso.GESTOR)) {
            try {
                listaUsuarios = usuarioRepository.findAll();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return listaUsuarios.toString();
        }
        else {
             throw new NoAutorizadoException("El usuario no cuenta con los permisos necesarios para realizar esta accion");
        }
    }

    public Optional<UsuarioEntity> buscarPorDni(Integer dni,UsuarioEntity logueado)throws NoAutorizadoException{
        Optional<UsuarioEntity> buscado = Optional.of(new UsuarioEntity());
        if (logueado.getCredencial().getPermiso().equals(EPermiso.ADMINISTRADOR) ||
        logueado.getCredencial().getPermiso().equals(EPermiso.GESTOR)){
            try {
                ArrayList<UsuarioEntity> listaUsuarios = usuarioRepository.findAll();
                buscado = listaUsuarios.stream().
                        filter(usuarioEntity -> usuarioEntity.getDni().equals(dni))
                        .findFirst();
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }else {
            throw new NoAutorizadoException("El usuario no cuenta con los permisos necesarios para realizar esta accion");
        }
        return buscado;
    }

    public Optional<UsuarioEntity> buscarPorEmail(String email,UsuarioEntity logueado)throws NoAutorizadoException{
        Optional<UsuarioEntity> buscado = Optional.of(new UsuarioEntity());
        if (logueado.getCredencial().getPermiso().equals(EPermiso.ADMINISTRADOR) ||
                logueado.getCredencial().getPermiso().equals(EPermiso.GESTOR)){
            try {
                ArrayList<UsuarioEntity> listaUsuarios = usuarioRepository.findAll();
                buscado = listaUsuarios.stream().
                        filter(usuarioEntity -> usuarioEntity.getEmail().equals(email))
                        .findFirst();
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }else {
            throw new NoAutorizadoException("El usuario no cuenta con los permisos necesarios para realizar esta accion");
        }
        return buscado;
    }
    public void eliminarCuenta (UsuarioEntity logueado,Integer idAEliminar) throws NoAutorizadoException{
        Optional<UsuarioEntity> usuarioABorrar = Optional.of(new UsuarioEntity());
        switch (logueado.getCredencial().getPermiso()){
            case CLIENTE:
                throw new NoAutorizadoException("El usuario no cuenta con los permisos necesarios para realizar esta accion");
            case GESTOR:
                try {
                    usuarioABorrar = usuarioRepository.findById(idAEliminar);
                    if (usuarioABorrar.isPresent()) {
                        Optional<CredencialEntity> credencial = credencialRepository.findByIdUser(usuarioABorrar.get().getId_usuario());
                        if(credencial.isPresent()){
                            usuarioABorrar.get().setCredencial(credencial.get());
                            if (usuarioABorrar.get().getCredencial().getPermiso().equals(EPermiso.CLIENTE)) {
                                usuarioRepository.deleteById(idAEliminar);
                                credencialRepository.deleteByIdUser(idAEliminar);
                                cuentaRepository.deleteByIdUser(idAEliminar);
                                System.out.println("El usuario se elimino con exito");
                            } else {
                                throw new NoAutorizadoException("El usuario no cuenta con los permisos necesarios para realizar esta accion");
                            }
                        }
                    } else {
                        System.out.println("No se encontro un usuario con ese id");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case ADMINISTRADOR:
                try {
                    usuarioRepository.deleteById(idAEliminar);
                    credencialRepository.deleteByIdUser(idAEliminar);
                    cuentaRepository.deleteByIdUser(idAEliminar);
                    System.out.println("El usuario se elimino con exito");
                }catch (SQLException e){
                    System.out.println("Error al conectarse a la base de datos");
                }
                break;
        }
    }
    public UsuarioEntity usuarioConMayorSaldo (UsuarioEntity usuarioLogueado) throws NoAutorizadoException{
        if (!usuarioLogueado.getCredencial().getPermiso().equals(EPermiso.ADMINISTRADOR)){
            throw new NoAutorizadoException("El usuario no esta autorizado a realizar esta accion");
        }
        Optional<UsuarioEntity> usuarioMayorSaldo = Optional.of(new UsuarioEntity());
        try {
            ArrayList<CuentaEntity> cuentas = cuentaRepository.findAll();
            Map<Integer,Double> mapa = cuentas.stream().
                    collect(Collectors.groupingBy(
                            CuentaEntity::getId_usuario,
                            Collectors.summingDouble(cuenta -> (double) cuenta.getSaldo())
                    ));
            ///Mapa donde voy a guardar solo el par clave-valor del mapa original cuyo valor sea el maximo, es decir el maximo monto
            Optional<Map.Entry<Integer,Double>> maximoValor = mapa.entrySet().stream()
                    .max(Map.Entry.comparingByValue());
            if(maximoValor.isPresent()){
                int id_usuarioMaximo = maximoValor.get().getKey();
                usuarioMayorSaldo = usuarioRepository.findById(id_usuarioMaximo);
                if(usuarioMayorSaldo.isPresent()){
                    return usuarioMayorSaldo.get();
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en la conexion a la base de datos: " + e.getMessage());
        }
        return null; ///Teniendo en cuenta que siempre hay usuarios en el sistema, no deberia retornar nunca null
    }

    public List<UsuarioEntity> listadosPorSaldoTotal (UsuarioEntity usuarioLogueado) throws NoAutorizadoException {
        if (!usuarioLogueado.getCredencial().getPermiso().equals(EPermiso.ADMINISTRADOR)) {
            throw new NoAutorizadoException("El usuario no esta autorizado a realizar esta accion");
        }
        try {
            Map<Integer, Double> mapa = cuentaRepository.findAll()
                    .stream().
                    collect(Collectors.groupingBy(
                            CuentaEntity::getId_usuario,
                            Collectors.summingDouble(cuenta -> (double) cuenta.getSaldo())
                    ));

            // Ordenás los IDs por saldo de forma descendente
            List<Integer> idsOrdenados = mapa.entrySet().stream() //Uso entry set para tener cada entrada del mapa, con su clave y valor y convierto eso en stream
                    .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed()) //Ordeno el stream segun el valor almacenado en cada entrada del mapa, que es el monto total (reversed para que sea de mayor a menor)
                    .map(Map.Entry::getKey) ///Una vez ordenada, convierto cada entrada en su key, es decir el id del usuario. Asi pierdo el value de cada entrada, pero ya no lo necesito
                    .toList(); //Retorno una lista con los ids de usuarios, que esta ordenada segun sus saldo

            // Ahora, creo una lista y por cada Id de la lista anterior (ordenada) busco el usuario correspondiente
            List<UsuarioEntity> usuariosOrdenados = new ArrayList<>();
            for (Integer id : idsOrdenados) {
                Optional<UsuarioEntity> usuario = usuarioRepository.findById(id);
                usuario.ifPresent(usuariosOrdenados::add);
            }
            return usuariosOrdenados;

        } catch (SQLException e) {
            System.out.println("Ocurrio un problema con la conexion en la base de datos: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    /*public void modificarDatosUsuario(UsuarioEntity logueado,String email, String nombre, String apellido){

        switch (logueado.getCredencial().getPermiso()) {
            case CLIENTE:
                try {
                    logueado.setNombre(nombre);
                    logueado.setApellido(apellido);
                    logueado.setEmail(email);
                    usuarioRepository.update(logueado);
                    System.out.println("Informacion actualizada correctamente");
                } catch (SQLException e) {
                    System.out.println("Error: Ya existe una cuenta asociada al email ingresado");
                }
                break;
            case GESTOR:

        }
    } */
}
