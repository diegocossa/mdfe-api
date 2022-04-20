package br.com.conexao.mdfe.api.repository.seguradora.impl;

import br.com.conexao.mdfe.api.model.seguradora.Seguradora;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SeguradoraRepositoryQuery {
    Page<Seguradora> filter(Seguradora seguradora, Pageable pageable);
}
