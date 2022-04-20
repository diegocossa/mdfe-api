package br.com.conexao.mdfe.api.repository.ciot;

import br.com.conexao.mdfe.api.model.ciot.Ciot;
import br.com.conexao.mdfe.api.model.empresa.Empresa;
import br.com.conexao.mdfe.api.model.motorista.Motorista;
import br.com.conexao.mdfe.api.repository.ciot.impl.CiotRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CiotRepository extends JpaRepository<Ciot, Long>, CiotRepositoryQuery {

    public List<Ciot> findAllByFlagdelIsFalse();

    public Ciot findByIdciotAndFlagdelIsFalse(Long idciot);

    public Ciot findByEmpresaAndFlagdelIsFalse(Empresa empresa);

    public Ciot findByMotoristaAndFlagdelIsFalse(Motorista motorista);
}
