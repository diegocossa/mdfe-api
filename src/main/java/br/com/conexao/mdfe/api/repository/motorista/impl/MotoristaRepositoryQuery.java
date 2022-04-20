package br.com.conexao.mdfe.api.repository.motorista.impl;

import br.com.conexao.mdfe.api.model.motorista.Motorista;
import br.com.conexao.mdfe.api.model.motorista.MotoristaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MotoristaRepositoryQuery {
    Page<Motorista> filter(MotoristaProjection motoristaProjection, Pageable pageable);
}
