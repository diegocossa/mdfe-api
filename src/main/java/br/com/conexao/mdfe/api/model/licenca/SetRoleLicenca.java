package br.com.conexao.mdfe.api.model.licenca;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.Collection;

public class SetRoleLicenca {

    //Função que seta roles em tempo de execução
    public Collection<GrantedAuthority> setar(EntityManager entityManager, String tenantid, Collection<GrantedAuthority> authorities) {
        try {
            ControleLicenca controleLicenca = new ControleLicenca();
            ControleAuthentications controleAuthentications = new ControleAuthentications();

            Timestamp dataFimLicenca = (Timestamp) entityManager.createNativeQuery("select dtfim from " + tenantid + ".mdfe_licenca order by idmdfelicenca desc limit 1").getSingleResult();

            Boolean isVencida = controleLicenca.isVencida(dataFimLicenca.toLocalDateTime());
            authorities = controleAuthentications.atualizarPermissao("LICENCA_NAO_VENCIDA", !isVencida, authorities);

            if (isVencida) {

                //Remove as telas de movimentação e cadastro quando a licença estiver vencida.
                authorities = controleAuthentications.atualizarPermissao("ROLE_CADASTRAR_CERTIFICADO", false, authorities);
                authorities = controleAuthentications.atualizarPermissao("ROLE_CADASTRAR_CIOT", false, authorities);
                authorities = controleAuthentications.atualizarPermissao("ROLE_CADASTRAR_EMPRESA", false, authorities);
                authorities = controleAuthentications.atualizarPermissao("ROLE_CADASTRAR_GRUPO", false, authorities);
                authorities = controleAuthentications.atualizarPermissao("ROLE_CADASTRAR_MDFE", false, authorities);
                authorities = controleAuthentications.atualizarPermissao("ROLE_CADASTRAR_MOTORISTA", false, authorities);
                authorities = controleAuthentications.atualizarPermissao("ROLE_CADASTRAR_PESSOA", false, authorities);
                authorities = controleAuthentications.atualizarPermissao("ROLE_CADASTRAR_SEGURADORA", false, authorities);
                authorities = controleAuthentications.atualizarPermissao("ROLE_CADASTRAR_USUARIO", false, authorities);
                authorities = controleAuthentications.atualizarPermissao("ROLE_CADASTRAR_VEICULO", false, authorities);
                authorities = controleAuthentications.atualizarPermissao("ROLE_CADASTRAR_CONFIGURACAO", false, authorities);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authorities;
    }
}
