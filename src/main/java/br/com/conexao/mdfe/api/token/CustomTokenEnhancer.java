package br.com.conexao.mdfe.api.token;

import br.com.conexao.mdfe.api.model.licenca.SetRoleLicenca;
import br.com.conexao.mdfe.api.security.PessoaSistema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

public class CustomTokenEnhancer implements TokenEnhancer {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        PessoaSistema pessoaSistema = (PessoaSistema) authentication.getPrincipal();

        Map<String, Object> addInfo = new HashMap<>();
        addInfo.put("CNXS", pessoaSistema.getPessoaLogin().getTenantid());
        addInfo.put("Nome", pessoaSistema.getPessoaLogin().getNome());
        addInfo.put("Code", pessoaSistema.getPessoaLogin().getIdpessoatenant());
        addInfo.put("CNXE", pessoaSistema.getPessoaLogin().getIdempresapadrao());
        addInfo.put("CNXTU", pessoaSistema.getPessoaLogin().getTipousuario());
        addInfo.put("userId", pessoaSistema.getPessoaLogin().getIdpessoa());

        Collection<GrantedAuthority> autorizacoes;

        SetRoleLicenca setRoleLicenca = new SetRoleLicenca();
        autorizacoes = setRoleLicenca.setar(entityManager, pessoaSistema.getPessoaLogin().getTenantid(), pessoaSistema.getAuthorities());

        List<String> authorities = new ArrayList<>();
        autorizacoes.forEach(x -> authorities.add(x.getAuthority()));

        addInfo.put("authorities", authorities);

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);
        return accessToken;
    }

}
