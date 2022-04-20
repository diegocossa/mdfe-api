package br.com.conexao.mdfe.api.config.provider;

import br.com.conexao.mdfe.api.model.pessoa.PessoaLogin;
import br.com.conexao.mdfe.api.repository.pessoa.PessoaLoginRepository;
import br.com.conexao.mdfe.api.security.PessoaSistema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Qualifier("AppUserDetailsService")
@Transactional
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private PessoaLoginRepository pessoaRepository;

    private Collection<? extends GrantedAuthority> getPermissoes(PessoaLogin pessoa) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        List<String> permissoes = pessoaRepository.permissoes(pessoa);

        permissoes.forEach(p -> authorities.add(new SimpleGrantedAuthority(p.toUpperCase())));

        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<PessoaLogin> pessoaOptional = pessoaRepository.emailEAtivo(email);

        PessoaLogin pessoa = null;

        if (!pessoaOptional.isPresent()) {
            Optional<PessoaLogin> motoristaOptional = pessoaRepository.cpfEAtivo(email);
            pessoa = motoristaOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));

        } else {
            pessoa = pessoaOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
        }

        return new PessoaSistema(pessoa, getPermissoes(pessoa));
    }
}
