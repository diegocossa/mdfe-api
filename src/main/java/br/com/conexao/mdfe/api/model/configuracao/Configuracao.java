package br.com.conexao.mdfe.api.model.configuracao;

import br.com.conexao.mdfe.api.model.tenancy.TenancyListaFilter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Configuracao extends TenancyListaFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idconfiguracao;

    @NotBlank
    @Size(max = 150)
    @Enumerated(EnumType.STRING)
    private TipoConfiguracaoEnum chave;

    @NotBlank
    @Size(max = 300)
    private String valor;
}
