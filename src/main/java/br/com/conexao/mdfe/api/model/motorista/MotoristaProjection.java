package br.com.conexao.mdfe.api.model.motorista;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MotoristaProjection {
    private String nome;
    private String cpf;
}
