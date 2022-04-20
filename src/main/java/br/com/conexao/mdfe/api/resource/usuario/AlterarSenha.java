package br.com.conexao.mdfe.api.resource.usuario;

import br.com.conexao.mdfe.api.model.pessoa.Usuario;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AlterarSenha {
    @NotNull
    private Long idUsuario;

    @NotNull
    private String senhaAtual;

    @NotNull
    private String novaSenha;
}
