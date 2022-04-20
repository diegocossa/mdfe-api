package br.com.conexao.mdfe.api.model.pessoa;

import br.com.conexao.mdfe.api.model.grupo.Grupo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idpessoa;

    @NotBlank
    private String tenantid;

    private Long idempresapadrao;

    @NotBlank
    @Size(max = 40)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 200)
    private String email;

    @Size(max = 11)
    private String cpf;

    @Size(max = 15)
    private String fone;

    @Enumerated(value = EnumType.STRING)
    private TipoUsuario tipousuario;

    @JsonIgnore
    private String senhausuario;

    private Boolean flagdel = false;

    private Boolean flagativo = true;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "pessoa_grupo", joinColumns = @JoinColumn(name = "idpessoa"), inverseJoinColumns = @JoinColumn(name = "idgrupo"))
    private List<Grupo> grupos;

    @JsonIgnore
    public void addGrupo(Grupo grupo){
        if(grupos == null){
            this.grupos = new ArrayList<>();
        }
        this.grupos.add(grupo);
    }
}
