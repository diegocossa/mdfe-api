package br.com.conexao.mdfe.api.model.mdfe.generica;

import br.com.conexao.mdfe.api.model.mdfe.MDFe;
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
public class InfMunCarrega extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idinfmuncarrega;
    private String cmuncarrega;
    private String xmuncarrega;

    @ManyToOne(cascade = {javax.persistence.CascadeType.ALL, javax.persistence.CascadeType.REFRESH})
    @JoinColumn(name = "idmdfe")
    @JsonBackReference("mdfe")
    private MDFe mdfe;
}

