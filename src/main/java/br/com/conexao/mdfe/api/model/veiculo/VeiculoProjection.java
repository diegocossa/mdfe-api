package br.com.conexao.mdfe.api.model.veiculo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoProjection {
    private String placa;
    private VeiculoTipo veiculoTipo;
}
