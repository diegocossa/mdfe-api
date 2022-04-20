package br.com.conexao.mdfe.api.model.grupo;

import br.com.conexao.mdfe.api.model.permissao.Permissao;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idgrupo;

    @Size(min=6, max = 40)
    private String nome;

    private Boolean flagdel = false;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "grupo_permissao",
            joinColumns = @JoinColumn(name = "idgrupo"), inverseJoinColumns = @JoinColumn(name = "idpermissao"))
    private List<Permissao> permissoes;
}
