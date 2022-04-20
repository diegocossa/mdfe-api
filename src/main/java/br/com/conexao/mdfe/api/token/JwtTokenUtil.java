package br.com.conexao.mdfe.api.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtTokenUtil {

    //Não alterar a senha daqui pois a função é static e gera outros problemas dentro do Jwt
    private static String encrypt = "8!)Ml^5xcXo$TNPA|z3()qW$eiIe9R:i](wg#R]G";

    public static String getTenantIdFromToken(String token){

        Claims claims = Jwts.parser().setSigningKey(encrypt.getBytes()).parseClaimsJws(token).getBody();

        return claims.get("CNXS").toString();
    }
}
