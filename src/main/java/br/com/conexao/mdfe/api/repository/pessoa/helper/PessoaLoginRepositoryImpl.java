package br.com.conexao.mdfe.api.repository.pessoa.helper;

import br.com.conexao.mdfe.api.model.pessoa.PessoaLogin;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public class PessoaLoginRepositoryImpl implements PessoaLoginRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PessoaLogin> emailEAtivo(String email) {
        return entityManager.createQuery("from PessoaLogin where lower(email) = lower(:email) and flagativo = true", PessoaLogin.class)
                            .setParameter("email", email).getResultList().stream().findFirst();
    }

    @Override
    public Optional<PessoaLogin> cpfEAtivo(String cpf) {
        return entityManager.createQuery("from PessoaLogin where lower(cpf) = lower(:cpf) and flagativo = true", PessoaLogin.class)
                .setParameter("cpf", cpf).getResultList().stream().findFirst();
    }

    @Override
    public List<String> permissoes(PessoaLogin pessoa) {
        String tenantId = pessoa.getTenantid();

        return entityManager.createNativeQuery("select\n" +
                "  DISTINCT per.nome\n" +
                "from\n" +
                ""+tenantId+".permissao per\n" +
                "    inner join "+tenantId+".grupo_permissao gru on (per.idpermissao = gru.idpermissao)\n" +
                "    inner join "+tenantId+".pessoa_grupo pes on (pes.idgrupo = gru.idgrupo)\n" +
                "    inner join "+tenantId+".grupo g on (pes.idgrupo = g.idgrupo)\n" +
                "    inner join "+tenantId+".pessoa p on (pes.idpessoa = p.idpessoa)\n" +
                "where\n" +
                "  p.idpessoa = :idPessoa\n" +
                "  and g.flagdel = false")
                .setParameter("idPessoa", pessoa.getIdpessoatenant())
                .getResultList();
    }
}
