package Services;

import Repositories.impl.CredencialRepository;

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
}
