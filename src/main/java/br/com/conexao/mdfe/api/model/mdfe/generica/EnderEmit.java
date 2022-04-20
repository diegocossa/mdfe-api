package br.com.conexao.mdfe.api.model.mdfe.generica;

import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
public class EnderEmit extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idenderemit;

    private String xLgr;
    private String nro;
    private String xCpl;
    private String xBairro;
    private String cMun;
    private String descricaoMun;
    private String CEP;

    @Enumerated(value = EnumType.STRING)
    private DFUnidadeFederativa UF;

    private String fone;
    private String email;
}

