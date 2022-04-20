package br.com.conexao.mdfe.api.repository.empresa;

import br.com.conexao.mdfe.api.model.empresa.Empresa;
import br.com.conexao.mdfe.api.repository.empresa.impl.EmpresaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>, EmpresaRepositoryQuery {

    public List<Empresa> findAllByFlagdelIsFalse();

    public Empresa findByIdempresaAndFlagdelIsFalse(Long idempresa);

}
