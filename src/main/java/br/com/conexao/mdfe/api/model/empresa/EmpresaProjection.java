package br.com.conexao.mdfe.api.model.empresa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaProjection {
    private String cnpj;
    private String razaosocial;
    private String fantasia;
}
