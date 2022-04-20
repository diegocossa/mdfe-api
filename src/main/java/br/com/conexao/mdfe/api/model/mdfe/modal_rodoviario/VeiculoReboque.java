package br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario;

import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import br.com.conexao.mdfe.api.model.veiculo.Veiculo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
public class VeiculoReboque extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idveiculoreboque;

    @NotNull
    @ManyToOne(cascade = {javax.persistence.CascadeType.ALL, javax.persistence.CascadeType.REFRESH})
    @JoinColumn(name = "idmdfe")
    @JsonBackReference("mdfe")
    private MDFe mdfe;

    @NotNull
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "idveiculo")
    private Veiculo veiculo;
}
