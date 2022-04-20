package br.com.conexao.mdfe.api.model.licenca;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ControleAuthentications implements Serializable {

    public Collection<GrantedAuthority> atualizarPermissao(String permissao, boolean adicionar, Collection<GrantedAuthority> authoritiesLocais) {

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
                authoritiesLocais);

        if (adicionar) {
            authorities.add(new SimpleGrantedAuthority(permissao));
        } else {
            authorities.remove(new SimpleGrantedAuthority(permissao));
        }

        authoritiesLocais = authorities;

        return authoritiesLocais;

    }
}