package br.com.conexao.mdfe.api.model.mdfe;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class DAMdfeEnvioEmail {

    @NotNull
    private Long idmdfe;

    private List<String> email;
}
