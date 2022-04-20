package br.com.conexao.mdfe.api.model.mdfe.generica;

import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
public class LacreUnidTransp extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idlacreunidtransp;

    private String nlacre;//character varying(20),

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idinfunidtransp")
    @JsonBackReference("infunidtransp")
    private InfUnidTransp infunidtransp;
}
