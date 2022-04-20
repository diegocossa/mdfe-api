package br.com.conexao.mdfe.api.util;

import java.util.Random;

public class GeradorTenantId {

    private static Random rand = new Random();
    private static char[] letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static String gerar (int nCaracteres) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < nCaracteres; i++) {
            int ch = rand.nextInt (letras.length);
            sb.append (letras [ch]);
        }

        return sb.toString().toLowerCase();
    }

}
