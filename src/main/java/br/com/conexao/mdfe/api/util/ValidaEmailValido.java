package br.com.conexao.mdfe.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidaEmailValido {

    public static Boolean isValido(String email){
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
