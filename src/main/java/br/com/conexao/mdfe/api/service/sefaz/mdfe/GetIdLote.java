package br.com.conexao.mdfe.api.service.sefaz.mdfe;

import br.com.conexao.mdfe.api.tenant.TenantContext;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;

@Service
public class GetIdLote {

    @PersistenceContext
    private EntityManager entityManager;

    public BigInteger buscarProximoId(){

        // SELECT nextval('tenant."mdfe_idlote_seq"')

        String sql = "SELECT nextval('"+TenantContext.getCurrentTenant()+".\"mdfe_idlote_seq\"')";
        Query query = entityManager.createNativeQuery(sql);

        return (BigInteger) query.getSingleResult();
    }
}
