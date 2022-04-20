package br.com.conexao.mdfe.api.model.permissao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idpermissao;

    @NotNull
    @Size(max = 40)
    private String nome;

    @NotNull
    @Size(max = 100)
    private String descricao;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private PermissaoGrupo grupo;

    @NotNull
    private Boolean flagvisivel=true;
}
