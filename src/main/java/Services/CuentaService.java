package Services;

import Repositories.impl.CuentaRepository;

public class CuentaService {
    private final CuentaRepository cuentaRepository;
    private static CuentaService instance;

    private CuentaService(){
        cuentaRepository = CuentaRepository.getInstanceOf();
    }

    public static CuentaService getInstanceOf(){
        if (instance == null){
            instance = new CuentaService();
        }
        return instance;
    }
}
