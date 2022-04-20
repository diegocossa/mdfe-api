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
public class Periculosidade extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idpericulosidade;

    @ManyToOne(cascade = {javax.persistence.CascadeType.ALL, javax.persistence.CascadeType.REFRESH})
    @JoinColumn(name = "idinfdoc")
    @JsonBackReference("infdoc")
    private InfDoc infdoc;

    private String gremb;//character varying(6),
    private String nonu;//character varying(4),
    private String qtotprod;//character varying(20),
    private String qvoltipo;// character varying(60),
    private String xclarisco;//character varying(40),
    private String xnomeae;// character varying(150)
}
