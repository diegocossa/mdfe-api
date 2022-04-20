package br.com.conexao.mdfe.api.repository.ciot.impl;

import br.com.conexao.mdfe.api.model.ciot.Ciot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CiotRepositoryQuery {
    Page<Ciot> filter(Ciot ciot, Pageable pageable);
}
