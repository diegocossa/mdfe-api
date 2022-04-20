package br.com.conexao.mdfe.api.model.pessoa;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetornoCadastroUsuario {

    private Pessoa pessoa;

    private Long idpessoa;

    private String senha;
}
