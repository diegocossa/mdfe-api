package br.com.conexao.mdfe.api.model.mdfe;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class CancelamentoMdfe {

    private String chave;
    private String protocolo;

    @NotBlank
    private String motivo;
}
