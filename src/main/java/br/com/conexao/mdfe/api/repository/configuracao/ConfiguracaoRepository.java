package br.com.conexao.mdfe.api.repository.configuracao;

import br.com.conexao.mdfe.api.model.configuracao.Configuracao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {

    Configuracao findByIdconfiguracao(Long idconfiguracao);

    List<Configuracao> findAllByChaveNotNull();

    List<Configuracao> findAllByIdempresa(Long idempresa);
}
