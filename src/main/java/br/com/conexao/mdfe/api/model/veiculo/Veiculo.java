package br.com.conexao.mdfe.api.model.veiculo;

import br.com.conexao.mdfe.api.model.empresa.UF;
import br.com.conexao.mdfe.api.model.proprietario.Proprietario;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idveiculo;

    @Size(min = 1, max = 7)
    private String placa;

    private String renavam;

    private Long capacidadem3;

    @Enumerated(value = EnumType.STRING)
    private VeiculoTipo veiculotipo;

    @Enumerated(value = EnumType.STRING)
    private TipoRodado tiporodado;

    private Long capacidadekg;

    @Enumerated(value = EnumType.STRING)
    private UF uf;

    private Long tara;

    @Enumerated(value = EnumType.STRING)
    private TipoCarroceriaEnum tipocarroceria;

    @Enumerated(value = EnumType.STRING)
    private TipoPropriedade tipopropriedade;

    @Size(max = 200)
    private String observacao;

    private Boolean flagdel=false;

    @OneToOne
    @JoinColumn(name = "idproprietario")
    private Proprietario proprietario;

}
