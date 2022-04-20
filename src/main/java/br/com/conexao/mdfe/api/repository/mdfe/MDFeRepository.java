package br.com.conexao.mdfe.api.repository.mdfe;

import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.repository.mdfe.filter.MdfeTotalizadorFilter;
import br.com.conexao.mdfe.api.repository.mdfe.impl.MDFeRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MDFeRepository extends JpaRepository<MDFe, Long>, MDFeRepositoryQuery {

    public MDFe findByIdmdfe(Long idmdfe);

    public MDFe findByChave(String chave);

    @Query(value = "select new br.com.conexao.mdfe.api.repository.mdfe.filter.MdfeTotalizadorFilter(count(m.situacao), m.situacao) from mdfe as m where (lower(ambienteenvio) = lower(?1) or ambienteenvio is null) group by m.situacao")
    List<MdfeTotalizadorFilter> getTotalizador(String ambienteenvio);
}
