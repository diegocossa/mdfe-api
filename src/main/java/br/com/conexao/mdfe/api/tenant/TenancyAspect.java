package br.com.conexao.mdfe.api.tenant;

import br.com.conexao.mdfe.api.tenant.hibernate.TenantListEmpresasContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Aspect
@Component
@Transactional(propagation = Propagation.REQUIRED)
public class TenancyAspect {

    @Autowired
    private EntityManager manager;

    @Before("execution(* br.com.conexao.mdfe.api.repository.*.*.*(..))")
    public void definirTenant() {
        Long empresaId = TenantIdEmpresaContext.getCurrentTenant();
        if (empresaId != null) {
            manager.unwrap(Session.class).enableFilter("empresa").setParameter("id", empresaId);
        }

        List<Long> listEmpresas = TenantListEmpresasContext.getCurrentList();
        if (listEmpresas != null) {
            manager.unwrap(Session.class).enableFilter("empresas").setParameterList("id", listEmpresas);
        }
    }
}
