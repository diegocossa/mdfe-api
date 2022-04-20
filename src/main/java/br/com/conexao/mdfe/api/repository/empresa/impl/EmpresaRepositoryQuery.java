package br.com.conexao.mdfe.api.repository.empresa.impl;

import br.com.conexao.mdfe.api.model.empresa.Empresa;
import br.com.conexao.mdfe.api.model.empresa.EmpresaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmpresaRepositoryQuery {
    Page<Empresa> filter(EmpresaProjection empresaProjection, Pageable pageable);
}
