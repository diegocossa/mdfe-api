package br.com.conexao.mdfe.api.resource.usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NovaSenha {

    private String senha;

    private String senhaConfirmacao;

    private String id;

    private String chk;
}
