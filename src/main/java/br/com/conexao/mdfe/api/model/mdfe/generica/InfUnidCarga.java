package br.com.conexao.mdfe.api.model.mdfe.generica;

import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fincatto.documentofiscal.mdfe3.classes.def.MDFTipoUnidadeCarga;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
public class InfUnidCarga extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idinfunidcarga;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idinfunidtransp")
    @JsonBackReference("infunidtransp")
    private InfUnidTransp infunidtransp;

    @Enumerated(value = EnumType.STRING)
    private MDFTipoUnidadeCarga tpunidcarga;

    private String idunidcarga;// character varying(20),
    private Double qtdrateada; //character varying(30),

    @ElementCollection
    @CollectionTable(
            name="lacre_unid_carga",
            joinColumns=@JoinColumn(name="idinfunidcarga")
    )
    @Column(name="nlacre")
    private Set<String> lacreunidcargalist= new HashSet<>();
}
