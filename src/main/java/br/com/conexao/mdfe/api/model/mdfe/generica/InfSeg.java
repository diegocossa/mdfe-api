package br.com.conexao.mdfe.api.model.mdfe.generica;

import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
public class InfSeg extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idinfseg;

    private String xSeg;
    private String CNPJ;
    private String nApol;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> nAver;
}
