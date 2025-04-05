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

        do {
            if (usuarioLogueado != null) {
                System.out.println("Ingrese 1 para cerrar sesion" +
                        "\nIngrese 2 para ver todos los usuarios" +
                        "\nIngrese 3 para buscar un usuario por DNI" +
                        "\nIngrese 4 para buscar un usuario por Email" +
                        "\nIngrese 5 para modificar un usuario" +
                        "\nIngrese 6 para eliminar un usuario");
                int opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1:
                        usuarioLogueado = null;
                        break;
                    case 2:
                        try {
                            System.out.println(usuarioService.listarTodosUsuarios(usuarioLogueado));
                        } catch (NoAutorizadoException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 3:
                        System.out.println("Ingrese DNI del usuario: ");
                        int dniBuscado = scanner.nextInt();
                        scanner.nextLine();
                        try {
                            Optional<UsuarioEntity> resultado = usuarioService.buscarPorDni(dniBuscado,usuarioLogueado);
                            resultado.ifPresentOrElse(
                                    System.out::println,
                                    () -> System.out.println("No existe usuario con ese DNI")
                            );
                        }catch (NoAutorizadoException e){
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 4:
                        System.out.println("Ingrese Email del usuario: ");
                        String emailBuscado = scanner.nextLine();
                        try {
                            Optional<UsuarioEntity> resultado = usuarioService.buscarPorEmail(emailBuscado,usuarioLogueado);
                            resultado.ifPresentOrElse(
                                    System.out::println,
                                    () -> System.out.println("No existe usuario con ese Email")
                            );
                        }catch (NoAutorizadoException e){
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 5:
                        System.out.println("En proceso");
                        break;
                    case 6:
                        System.out.println("Ingrese la id del usuario a eliminar:");
                        int idAEliminar = scanner.nextInt();
                        scanner.nextLine();
                        try {
                            usuarioService.eliminarCuenta(usuarioLogueado,idAEliminar);
                        }catch (NoAutorizadoException e){
                            System.out.println(e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("Opcion ingresada invalida");
                        break;
                }
            }
        } while (usuarioLogueado != null);
    }
}
