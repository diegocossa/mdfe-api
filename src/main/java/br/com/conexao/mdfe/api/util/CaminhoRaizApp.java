package br.com.conexao.mdfe.api.util;

import java.io.File;
import java.io.IOException;

public class CaminhoRaizApp {

    //Retorna a pasta raiz da aplicação caso precise gravar arquivo em pastas
    public static String getCaminhoRaizApp(){
        String local = null;
        try {
            local = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return local;
    }
}
