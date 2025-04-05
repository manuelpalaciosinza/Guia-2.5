package Services;

import Entities.Classes.CredencialEntity;
import Entities.Classes.CuentaEntity;
import Entities.Classes.UsuarioEntity;
import Entities.Enum.EPermiso;
import Exceptions.NoAutorizadoException;
import Repositories.impl.CredencialRepository;
import Repositories.impl.CuentaRepository;
import Repositories.impl.UsuarioRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CuentaService {
    private final CuentaRepository cuentaRepository;
    private final CredencialRepository credencialRepository;
    private static CuentaService instance;

    private CuentaService() {
        cuentaRepository = CuentaRepository.getInstanceOf();
        credencialRepository = CredencialRepository.getInstanceOf();
    }

    public static CuentaService getInstanceOf() {
        if (instance == null) {
            instance = new CuentaService();
        }
        return instance;
    }

    public ArrayList<CuentaEntity> listaCuentasUsuario(UsuarioEntity logueado) {
        ArrayList<CuentaEntity> listaCuentas = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        if (!logueado.getCredencial().getPermiso().equals(EPermiso.CLIENTE)) {
            System.out.println("Ingrese la id del usuario cuyas cuentas quiere observar");
            int idCliente = scanner.nextInt();
            scanner.nextLine();
            try {
                listaCuentas = cuentaRepository.findAll().stream()
                        .filter(cuentaEntity -> cuentaEntity.getId_usuario().equals(idCliente))
                        .collect(Collectors.toCollection(ArrayList::new));
            } catch (SQLException e) {
                System.out.println("Error en la conexion con la base de datos: " + e.getMessage());
            }
        } else {
            try {
                listaCuentas = cuentaRepository.findAll().stream()
                        .filter(cuentaEntity -> cuentaEntity.getId_usuario().equals(logueado.getId_usuario()))
                        .collect(Collectors.toCollection(ArrayList::new));
            } catch (SQLException e) {
                System.out.println("Error en la conexion con la base de datos: " + e.getMessage());
            }
        }
        return listaCuentas;
    }

    public float calcularSaldoTotal(UsuarioEntity logueado) {
        ArrayList<CuentaEntity> listaCuentas = new ArrayList<>();
        float saldoTotal = 0;
        Scanner scanner = new Scanner(System.in);
        if (!logueado.getCredencial().getPermiso().equals(EPermiso.CLIENTE)) {
            System.out.println("Ingrese la id del usuario cuyo saldo quiere observar");
            int idCliente = scanner.nextInt();
            scanner.nextLine();
            try {
                saldoTotal = cuentaRepository.findAll().stream()
                        .filter(cuentaEntity -> cuentaEntity.getId_usuario().equals(idCliente))
                        .map(CuentaEntity::getSaldo)
                        .reduce(0.0f, Float::sum);
            } catch (SQLException e) {
                System.out.println("Error en la conexion con la base de datos: " + e.getMessage());
            }
        } else {
            try {
                saldoTotal = cuentaRepository.findAll().stream()
                        .filter(cuentaEntity -> cuentaEntity.getId_usuario().equals(logueado.getId_usuario()))
                        .map(CuentaEntity::getSaldo)
                        .reduce(0f,Float::sum);
            } catch (SQLException e) {
                System.out.println("Error en la conexion con la base de datos: " + e.getMessage());
            }
        }
        return saldoTotal;
    }
    public void depositarSaldo (UsuarioEntity logueado) throws NoAutorizadoException{
        Scanner scanner = new Scanner(System.in);
        int id_cuenta;
        Optional<CuentaEntity> cuentaADepositar = Optional.of(new CuentaEntity());
        float montoAdepositar;
        try {
            switch (logueado.getCredencial().getPermiso()) {
                case CLIENTE:
                    System.out.println("Listado de sus cuentas: " + listaCuentasUsuario(logueado));
                    System.out.println("Ingrese el id de la cuenta a la que quiere depositar: ");
                    id_cuenta = scanner.nextInt();
                    scanner.nextLine();
                    cuentaADepositar = cuentaRepository.findById(id_cuenta);
                    if (cuentaADepositar.isPresent() && cuentaADepositar.get().getId_usuario().equals(logueado.getId_usuario())){
                        System.out.println("Ingrese el monto a depositar: ");
                        montoAdepositar = scanner.nextFloat();
                        scanner.nextLine();
                        cuentaADepositar.get().setSaldo(cuentaADepositar.get().getSaldo() + montoAdepositar);
                        cuentaRepository.update(cuentaADepositar.get());
                    }else {
                        System.out.println("No existe cuenta con el id ingresado");
                    }
                    break;
                case GESTOR:
                    System.out.println("Listado de cuentas: " + cuentaRepository.findAll());
                    System.out.println("Ingrese el id de la cuenta a la que quiere depositar: ");
                    id_cuenta = scanner.nextInt();
                    scanner.nextLine();
                    cuentaADepositar = cuentaRepository.findById(id_cuenta);
                    if (cuentaADepositar.isPresent()){
                        Optional<CredencialEntity> credencialADepositar = credencialRepository.findByIdUser(cuentaADepositar.get().getId_usuario());
                        if (credencialADepositar.isPresent() && credencialADepositar.get().getPermiso().equals(EPermiso.CLIENTE)) {
                            System.out.println("Ingrese el monto a depositar: ");
                            montoAdepositar = scanner.nextFloat();
                            scanner.nextLine();
                            cuentaADepositar.get().setSaldo(cuentaADepositar.get().getSaldo() + montoAdepositar);
                            cuentaRepository.update(cuentaADepositar.get());
                        }
                        else {
                            throw new NoAutorizadoException("El usuario no tiene permisos para realizar esa accion");
                        }
                    }else {
                        System.out.println("No existe cuenta con el id ingresado");
                    }
                    break;
                case ADMINISTRADOR:
                    System.out.println("Listado de cuentas: " + cuentaRepository.findAll());
                    System.out.println("Ingrese el id de la cuenta a la que quiere depositar: ");
                    id_cuenta = scanner.nextInt();
                    scanner.nextLine();
                    cuentaADepositar = cuentaRepository.findById(id_cuenta);
                    if (cuentaADepositar.isPresent()){
                        System.out.println("Ingrese el monto a depositar: ");
                        montoAdepositar = scanner.nextFloat();
                        scanner.nextLine();
                        cuentaADepositar.get().setSaldo(cuentaADepositar.get().getSaldo() + montoAdepositar);
                        cuentaRepository.update(cuentaADepositar.get());
                    }else {
                        System.out.println("No existe cuenta con el id ingresado");
                    }
                    break;
            }
        }catch (SQLException e){
            System.out.println("Error en la conexion a la base de datos: " + e.getMessage());
        }
    }
}

