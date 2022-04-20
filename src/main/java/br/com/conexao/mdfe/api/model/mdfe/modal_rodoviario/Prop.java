package br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario;

import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.mdfe3.classes.def.MDFTipoProprietario;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
public class Prop extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idprop;

    private String CPF;
    private String CNPJ;
    private String RNTRC;
    private String xNome;
    private String IE;

    @Enumerated(value = EnumType.STRING)
    private DFUnidadeFederativa UF;

    @Enumerated(value = EnumType.STRING)
    private MDFTipoProprietario tpProp;
}
