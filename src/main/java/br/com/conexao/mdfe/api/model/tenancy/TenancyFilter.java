package br.com.conexao.mdfe.api.model.tenancy;

import br.com.conexao.mdfe.api.tenant.TenantIdEmpresaContext;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@FilterDef(name = "empresa", parameters = { @ParamDef(name="id", type="long") })
@Filter(name = "empresa", condition = "idempresa = :id")
@MappedSuperclass
@Getter
@Setter
public abstract class TenancyFilter {

    private Long idempresa;

    @PreUpdate
    @PrePersist
    public void definirTenant() {
        Long idEmpresa = TenantIdEmpresaContext.getCurrentTenant();
        if (idEmpresa != null) {
            setIdempresa(idEmpresa);
        }
    }
}

