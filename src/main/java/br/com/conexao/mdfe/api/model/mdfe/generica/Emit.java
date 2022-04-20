package br.com.conexao.mdfe.api.model.mdfe.generica;

import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
public class Emit extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idemit;

    private String CNPJ;
    private String IE;
    private String xNome;
    private String xFant;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idenderemit")
    private EnderEmit enderEmit;
}
