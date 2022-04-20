package br.com.conexao.mdfe.api.model.motorista;

import br.com.conexao.mdfe.api.model.pessoa.Pessoa;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Size;

import static java.util.Objects.isNull;

@Entity
@Getter
@Setter
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idmotorista;

    @NotBlank
    @Size(max = 60)
    private String nome;

    @NotBlank
    @Size(max = 11)
    @CPF
    private String cpf;

    @Transient
    private String senhamotorista;

    @Size(max = 20)
    private String telefone;

    @Size(max = 200)
    private String observacao;

    private Boolean flagativo=true;

    private Boolean flagdel =false;

    public Boolean novoMotorista(){
        return isNull(getIdmotorista());
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "pessoa_motorista",
            joinColumns = @JoinColumn(name = "idmotorista"), inverseJoinColumns = @JoinColumn(name = "idpessoa"))
    private Pessoa pessoa;
}
