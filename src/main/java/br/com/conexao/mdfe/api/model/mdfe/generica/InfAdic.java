package br.com.conexao.mdfe.api.model.mdfe.generica;

import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
public class InfAdic extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idinfadic;

    private String infAdFisco;
    private String infCpl;
}
