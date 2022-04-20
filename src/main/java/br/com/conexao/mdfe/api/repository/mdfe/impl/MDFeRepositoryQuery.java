package br.com.conexao.mdfe.api.repository.mdfe.impl;

import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.repository.mdfe.filter.MdfeFilter;
import br.com.conexao.mdfe.api.repository.mdfe.projection.MDFeProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MDFeRepositoryQuery {
    Page<MDFe> filter(MdfeFilter mdfeFilter, Pageable pageable);
    Page<MDFeProjection> resume(MdfeFilter mdfeFilter, Pageable pageable);
}

