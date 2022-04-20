package br.com.conexao.mdfe.api.model.tenancy;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.MappedSuperclass;

@FilterDef(name = "empresas", parameters = { @ParamDef(name="id", type="long") })
@Filter(name = "empresas", condition = "idempresa in (:id)")
@MappedSuperclass
public abstract class TenancyListaFilter extends TenancyFilter {

}