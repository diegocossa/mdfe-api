package br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario;

import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
public class ValePedagio extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idvalepedagio;

    private String cnpjforn;//character varying(14),
    private String cnpjpg;//character varying(14),
    private String cpfpg;//character varying(11),
    private String ncompra;//character varying(20)
    private BigDecimal vvaleped;

    @ManyToOne(cascade = {javax.persistence.CascadeType.ALL, javax.persistence.CascadeType.REFRESH})
    @JoinColumn(name = "idmdfe")
    @JsonBackReference("mdfe")
    private MDFe mdfe;
}
