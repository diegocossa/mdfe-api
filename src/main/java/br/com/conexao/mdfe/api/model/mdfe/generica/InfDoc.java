package br.com.conexao.mdfe.api.model.mdfe.generica;

import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
public class InfDoc extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idinfdoc;

    @ManyToOne(cascade = {javax.persistence.CascadeType.ALL, javax.persistence.CascadeType.REFRESH})
    @JoinColumn(name = "idinfmundescarga")
    @JsonBackReference("infmundescarga")
    private InfMunDescarga infmundescarga;

    private String chavedoc;// character varying(44)

    @Enumerated(EnumType.STRING)
    private TipoDocEnum tipodoc;// character varying(4), -- nfe ou cte

    private String segcodbarra;// character varying(36),

    private Double peso;// decimal(11,4) not null,
    private Double valor;// decimal(13,2) not null,

    private Double qtdtotal;// decimal(11,4), qtdTotal
    private Double qtdparcial;// decimal(11,4), qtdParcial

    @OneToMany(mappedBy = "infdoc", cascade = CascadeType.ALL)
    @JsonManagedReference("infdoc")
    private List<InfUnidTransp> infunidtransplist = new ArrayList<>();

    @OneToMany(mappedBy = "infdoc", cascade = CascadeType.ALL)
    @JsonManagedReference("infdoc")
    private List<Periculosidade> periculosidadelist = new ArrayList<>();
}
