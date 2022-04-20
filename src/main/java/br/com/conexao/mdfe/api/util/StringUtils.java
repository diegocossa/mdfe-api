package br.com.conexao.mdfe.api.util;

public class StringUtils {

    public static String trim(String value) {

        if (value != null) {
            return value.trim();
        }

        return null;
    }
}
