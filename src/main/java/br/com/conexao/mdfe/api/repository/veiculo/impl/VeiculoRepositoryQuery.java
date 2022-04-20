package br.com.conexao.mdfe.api.repository.veiculo.impl;

import br.com.conexao.mdfe.api.model.veiculo.Veiculo;
import br.com.conexao.mdfe.api.model.veiculo.VeiculoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VeiculoRepositoryQuery {
    Page<Veiculo> filter(VeiculoProjection veiculoProjection, Pageable pageable);
}
