package br.com.conexao.mdfe.api.model.mdfe;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class NovoCondutor {

    @NotBlank
    private String chave;

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;
}
