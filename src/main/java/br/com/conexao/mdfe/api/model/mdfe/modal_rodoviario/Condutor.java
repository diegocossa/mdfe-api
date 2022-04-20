package br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario;

import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.model.motorista.Motorista;
import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@JsonIgnoreProperties(value = {"idempresa"})
@Getter
@Setter
@EqualsAndHashCode(exclude = "idcondutor", callSuper = false)
public class Condutor extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcondutor;

    @ManyToOne(cascade = {javax.persistence.CascadeType.ALL, javax.persistence.CascadeType.REFRESH})
    @JoinColumn(name = "idmdfe")
    @JsonBackReference("mdfe")
    private MDFe mdfe;

    @NotNull
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "idmotorista")
    private Motorista motorista;
}
