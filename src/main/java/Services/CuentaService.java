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
import java.util.*;
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

    public ArrayList<CuentaEntity> listaCuentasUsuarioCorregido (UsuarioEntity logueado,int id_usuario) throws NoAutorizadoException {
        if (!logueado.getId_usuario().equals(id_usuario) && logueado.getCredencial().getPermiso().equals(EPermiso.CLIENTE)) {
                throw new NoAutorizadoException("El usuario no tiene permisos para ver cuentas ajenas");
        }
        ArrayList<CuentaEntity> listaCuentas = new ArrayList<>();
        try {
            listaCuentas = cuentaRepository.findAll().stream()
                    .filter(cuentaEntity -> cuentaEntity.getId_usuario().equals(id_usuario))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (SQLException e) {
            System.out.println("Error en la conexion con la base de datos: " + e.getMessage());
        }
        return listaCuentas;
    }

    public float calcularSaldoTotalCorregido (UsuarioEntity logueado,int id_usuario) throws NoAutorizadoException {
        ArrayList<CuentaEntity> listaCuentas = new ArrayList<>();
        float saldoTotal = 0;
        if (!logueado.getId_usuario().equals(id_usuario) && logueado.getCredencial().getPermiso().equals(EPermiso.CLIENTE)) {
           throw new NoAutorizadoException("El usuario solo puede ver el saldo de sus propias cuentas");
        }
        try {
            saldoTotal = cuentaRepository.findAll().stream()
                    .filter(cuentaEntity -> cuentaEntity.getId_usuario().equals(id_usuario))
                    .map(CuentaEntity::getSaldo)
                    .reduce(0.0f, Float::sum);
        } catch (SQLException e) {
            System.out.println("Error en la conexion con la base de datos: " + e.getMessage());
        }
        return saldoTotal;
    }

    public void depositarSaldoCorregido (UsuarioEntity logueado,float monto, int id_cuenta) throws NoAutorizadoException{
        Optional<CuentaEntity> cuentaADepositar = Optional.of(new CuentaEntity());
        try {
            switch (logueado.getCredencial().getPermiso()) {
                case CLIENTE:
                    cuentaADepositar = cuentaRepository.findById(id_cuenta);
                    if (cuentaADepositar.isPresent() && cuentaADepositar.get().getId_usuario().equals(logueado.getId_usuario())){
                        cuentaADepositar.get().setSaldo(cuentaADepositar.get().getSaldo() + monto);
                        cuentaRepository.update(cuentaADepositar.get());
                    }else {
                        throw new NoAutorizadoException("El usuario no esta autorizado para depositar a cuentas ajenas");
                    }
                    break;
                case GESTOR:
                    cuentaADepositar = cuentaRepository.findById(id_cuenta);
                    if (cuentaADepositar.isPresent()){
                        Optional<CredencialEntity> credencialADepositar = credencialRepository.findByIdUser(cuentaADepositar.get().getId_usuario());
                        if (credencialADepositar.isPresent() && credencialADepositar.get().getPermiso().equals(EPermiso.CLIENTE)) {
                            cuentaADepositar.get().setSaldo(cuentaADepositar.get().getSaldo() + monto);
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
                    cuentaADepositar = cuentaRepository.findById(id_cuenta);
                    if (cuentaADepositar.isPresent()){
                        cuentaADepositar.get().setSaldo(cuentaADepositar.get().getSaldo() + monto);
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
    public Map<String,Long> cantidadCuentasPorTipo (UsuarioEntity usuarioLogueado)throws NoAutorizadoException{
        Map<String,Long> resultado = new HashMap<>();
        if (usuarioLogueado.getCredencial().getPermiso().equals(EPermiso.CLIENTE))
        {
            throw new NoAutorizadoException("El usuario no cuenta con los permisos necesarios para realizar esta accion");
        }
        try {
            resultado = cuentaRepository.findAll().stream()
                    .collect(Collectors.groupingBy(cuentaEntity -> cuentaEntity.getTipo_cuenta().name(),Collectors.counting()));
        }catch (SQLException e){
            System.out.println("Error en la conexion a la base de datos: " + e.getMessage());
        }
        return resultado;
    }
}

