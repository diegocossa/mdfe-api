package br.com.conexao.mdfe.api.repository.seguradora;

import br.com.conexao.mdfe.api.model.seguradora.Seguradora;
import br.com.conexao.mdfe.api.repository.seguradora.impl.SeguradoraRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeguradoraRepository extends JpaRepository<Seguradora, Long>, SeguradoraRepositoryQuery {

    public List<Seguradora> findAllByFlagdelIsFalse();

    public Seguradora findByidSeguradoraAndFlagdelIsFalse(Long idseguradora);

}
