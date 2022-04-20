package br.com.conexao.mdfe.api.model.ciot;

import br.com.conexao.mdfe.api.model.empresa.Empresa;
import br.com.conexao.mdfe.api.model.motorista.Motorista;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Ciot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idciot;

    @NotBlank
    @Size(max = 11)
    private String ciot;

    @OneToOne
    @JoinColumn(name = "idempresa")
    private Empresa empresa;

    @OneToOne
    @JoinColumn(name = "idmotorista")
    private Motorista motorista;

    private Boolean flagdel=false;
}
