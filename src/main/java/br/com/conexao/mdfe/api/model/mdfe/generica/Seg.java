package br.com.conexao.mdfe.api.model.mdfe.generica;

import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.model.seguradora.Seguradora;
import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
public class Seg extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idseg;

    @NotNull
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "idseguradora")
    private Seguradora seguradora;

    @ManyToOne(cascade = {javax.persistence.CascadeType.ALL, javax.persistence.CascadeType.REFRESH})
    @JoinColumn(name = "idmdfe")
    @JsonBackReference("mdfe")
    private MDFe mdfe;

    @ElementCollection
    @CollectionTable(
            name="seg_numero_averbacao",
            joinColumns=@JoinColumn(name="idseg")
    )
    @Column(name="naver")
    private Set<String> segnumeroaverbacaolist = new HashSet<>();
}
