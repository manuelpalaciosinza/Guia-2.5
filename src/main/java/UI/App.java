package UI;

import Entities.Classes.CuentaEntity;
import Entities.Classes.UsuarioEntity;
import Exceptions.NoAutorizadoException;
import Repositories.impl.CredencialRepository;
import Repositories.impl.CuentaRepository;
import Repositories.impl.UsuarioRepository;
import Services.CredencialService;
import Services.CuentaService;
import Services.UsuarioService;

import java.sql.SQLException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class App {

    private final Scanner scanner;
    private final UsuarioService usuarioService;
    private final CredencialService credencialService;
    private final CuentaService cuentaService;

    public App() {
        scanner = new Scanner(System.in);
        usuarioService = UsuarioService.getInstanceOf();
        credencialService = CredencialService.getInstanceOf();
        cuentaService = CuentaService.getInstanceOf();
    }

    public void iniciarApp() {
        int seguir;
        UsuarioEntity usuarioLogueado = null;
        do {
            System.out.println("Ingrese 1 para registrarse o 2 para iniciar sesion. Ingrese 0 para cerrar la aplicacion");
            seguir = scanner.nextInt();
            scanner.nextLine();

            switch (seguir) {
                case 0:
                    System.out.println("Cerrando aplicacion...");
                    break;
                case 1:
                    registro();
                    break;
                case 2:
                    usuarioLogueado = logueo();
                    do {
                        if (usuarioLogueado != null) {
                            int opcion = menuOpciones();
                            switch (opcion) {
                                case 1:
                                    usuarioLogueado = null;
                                    System.out.println("Cerrando sesion...");
                                    break;
                                case 2:
                                    listarTodosUsuarios(usuarioLogueado);
                                    break;
                                case 3:
                                    buscarXDni(usuarioLogueado);
                                    break;
                                case 4:
                                    buscarXEmail(usuarioLogueado);
                                    break;
                                case 5:
                                    System.out.println("En proceso");
                                    break;
                                case 6:
                                    eliminarUsuario(usuarioLogueado);
                                    break;
                                case 7:
                                    System.out.println("Cuentas del usuario: " + cuentaService.listaCuentasUsuario(usuarioLogueado));
                                    break;
                                case 8:
                                    System.out.println("Saldo de todas las cuentas del usuario: " + cuentaService.calcularSaldoTotal(usuarioLogueado));
                                    break;
                                case 9:
                                    realizarDepositos(usuarioLogueado);
                                    break;
                                case 11:
                                    cantidadDeUsuariosPorPermiso(usuarioLogueado);
                                    break;
                                case 12:
                                    cantidadDeCuentasPorTipo(usuarioLogueado);
                                    break;
                                case 13:
                                    usuarioConMayorSaldo(usuarioLogueado);
                                    break;
                                case 14:
                                    usuariosOrdenadosPorSaldo(usuarioLogueado);
                                    break;
                                default:
                                    System.out.println("Opcion ingresada invalida");
                                    break;
                            }
                        }
                    } while (usuarioLogueado != null);
                    break;
                default:
                    System.out.println("Opcion ingresada invalida");
                    break;
            }
        } while (seguir != 0);
    }

    private void registro() {
        System.out.println("Ingrese su nombre: ");
        String nombre = scanner.nextLine();
        System.out.println("Ingrese su apellido: ");
        String apellido = scanner.nextLine();
        System.out.println("Ingrese su DNI: ");
        int dni = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Ingrese su email: ");
        String email = scanner.nextLine();
        usuarioService.registrarUsuario(nombre, apellido, dni, email);
    }

    private int menuOpciones() {
        System.out.println("Ingrese 1 para cerrar sesion" +
                "\nIngrese 2 para ver todos los usuarios" +
                "\nIngrese 3 para buscar un usuario por DNI" +
                "\nIngrese 4 para buscar un usuario por Email" +
                "\nIngrese 5 para modificar un usuario" +
                "\nIngrese 6 para eliminar un usuario" +
                "\nIngrese 7 para ver las cuentas de un usuario" +
                "\nIngrese 8 para ver el saldo de todas las cuentas de un usuario" +
                "\nIngrese 9 para realizar un deposito" +
                "\nIngrese 10 para realizar una transferencia" +
                "\nIngrese 11 para ver la cantidad de usuarios por permiso" +
                "\nIngrese 12 para ver la cantidad de cuentas por tipo" +
                "\nIngrese 13 para ver el usuario con mayor saldo en todas sus cuentas" +
                "\nIngrese 14 para ver los usuarios ordenados por saldo");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        return opcion;
    }

    private UsuarioEntity logueo() {
        UsuarioEntity usuarioLogueado = null;
        System.out.println("Ingrese su nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.println("Ingrese su contraseña");
        String contrasenia = scanner.nextLine();
        try {
            usuarioLogueado = usuarioService.iniciarSesion(username, contrasenia);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return usuarioLogueado;
    }

    private void listarTodosUsuarios(UsuarioEntity usuarioLogueado) {
        try {
            System.out.println(usuarioService.listarTodosUsuarios(usuarioLogueado));
        } catch (NoAutorizadoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void buscarXDni(UsuarioEntity usuarioLogueado) {
        System.out.println("Ingrese DNI del usuario: ");
        int dniBuscado = scanner.nextInt();
        scanner.nextLine();
        try {
            Optional<UsuarioEntity> resultado = usuarioService.buscarPorDni(dniBuscado, usuarioLogueado);
            resultado.ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("No existe usuario con ese DNI")
            );
        } catch (NoAutorizadoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void buscarXEmail(UsuarioEntity usuarioLogueado) {
        System.out.println("Ingrese Email del usuario: ");
        String emailBuscado = scanner.nextLine();
        try {
            Optional<UsuarioEntity> resultado = usuarioService.buscarPorEmail(emailBuscado, usuarioLogueado);
            resultado.ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("No existe usuario con ese Email")
            );
        } catch (NoAutorizadoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void eliminarUsuario(UsuarioEntity usuarioLogueado) {
        System.out.println("Ingrese la id del usuario a eliminar:");
        int idAEliminar = scanner.nextInt();
        scanner.nextLine();
        try {
            usuarioService.eliminarCuenta(usuarioLogueado, idAEliminar);
        } catch (NoAutorizadoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void realizarDepositos(UsuarioEntity usuarioLogueado) {
        try {
            cuentaService.depositarSaldo(usuarioLogueado);
        } catch (NoAutorizadoException e) {
            System.out.println(e.getMessage());
        }
    }
    private void cantidadDeUsuariosPorPermiso (UsuarioEntity usuarioLogueado){
        try {
            Map<String,Long> usuariosPorPermiso = credencialService.obtenerUsuariosPorPermiso(usuarioLogueado);
            System.out.println(usuariosPorPermiso.toString());
        }catch (NoAutorizadoException e){
            System.out.println(e.getMessage());
        }
    }
    private void cantidadDeCuentasPorTipo (UsuarioEntity usuarioLogueado){
        try {
            Map<String,Long> cuentasPorTipo = cuentaService.cantidadCuentasPorTipo(usuarioLogueado);
            System.out.println(cuentasPorTipo.toString());
        }catch (NoAutorizadoException e){
            System.out.println(e.getMessage());
        }
    }
    private void usuarioConMayorSaldo(UsuarioEntity usuarioLogueado){
        try {
            UsuarioEntity usuario = usuarioService.usuarioConMayorSaldo(usuarioLogueado);
            if(usuario != null){
                System.out.println("Usuario con mayor saldo: " + usuario.toString());
            }else {
                System.out.println("No existen usuarios en el sistema");
            }
        }catch (NoAutorizadoException e){
            System.out.println(e.getMessage());
        }
    }
    private void usuariosOrdenadosPorSaldo (UsuarioEntity usuarioLogueado){
        try {
            System.out.println("Lista de usuarios ordenada por sus saldos: " + usuarioService.listadosPorSaldoTotal(usuarioLogueado));
        }catch (NoAutorizadoException e){
            System.out.println(e.getMessage());
        }
    }
}
