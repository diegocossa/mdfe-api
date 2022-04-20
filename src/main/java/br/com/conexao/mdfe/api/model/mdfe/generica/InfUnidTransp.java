package br.com.conexao.mdfe.api.model.mdfe.generica;

import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fincatto.documentofiscal.mdfe3.classes.def.MDFTipoUnidadeTransporte;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
public class InfUnidTransp extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idinfunidtransp;

    @Enumerated(value = EnumType.STRING)
    private MDFTipoUnidadeTransporte tpunidtransp;

    private String idunidtransp;// character varying(20),

    private Double qtdrateada;//qtdrateada numeric(5,2),

    @ManyToOne(cascade = {javax.persistence.CascadeType.ALL, javax.persistence.CascadeType.REFRESH})
    @JoinColumn(name = "idinfdoc")
    @JsonBackReference("infdoc")
    private InfDoc infdoc;

    @ElementCollection
    @CollectionTable(
            name="lacre_unid_transp",
            joinColumns=@JoinColumn(name="idinfunidtransp")
    )
    @Column(name="nlacre")
    private Set<String> lacreunidtransplist= new HashSet<>();

    @OneToMany(mappedBy = "infunidtransp", cascade = CascadeType.ALL)
    @JsonManagedReference("infunidtransp")
    private List<InfUnidCarga> infunidcargalist = new ArrayList<>();
}
