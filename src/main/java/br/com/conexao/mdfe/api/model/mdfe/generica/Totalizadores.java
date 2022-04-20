package br.com.conexao.mdfe.api.model.mdfe.generica;

import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fincatto.documentofiscal.mdfe3.classes.def.MDFUnidadeMedidaPesoBrutoCarga;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
public class Totalizadores extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idtotalizadores;

    private String qcte;
    private String qnfe;
    private String qmdfe;
    private BigDecimal vcarga;

    @Enumerated(value = EnumType.ORDINAL)
    private MDFUnidadeMedidaPesoBrutoCarga cunid;

    private BigDecimal qcarga;

    @ManyToOne(cascade = {javax.persistence.CascadeType.ALL, javax.persistence.CascadeType.REFRESH})
    @JoinColumn(name = "idmdfe")
    @JsonBackReference("mdfe")
    private MDFe mdfe;
}
