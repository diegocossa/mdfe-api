package br.com.conexao.mdfe.api.security;

import br.com.conexao.mdfe.api.model.pessoa.PessoaLogin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class PessoaSistema extends User{

    private PessoaLogin pessoaLogin;

    public PessoaSistema(PessoaLogin pessoaLogin, Collection<? extends GrantedAuthority> authorities) {
        super(pessoaLogin.getEmail(), pessoaLogin.getSenhausuario(), authorities);
        this.pessoaLogin = pessoaLogin;
    }

    public PessoaLogin getPessoaLogin() {
        return pessoaLogin;
    }
}
