package br.com.conexao.mdfe.api.repository.empresa;

import br.com.conexao.mdfe.api.model.empresa.SequenceNumMdf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceNumMdfRepository extends JpaRepository<SequenceNumMdf, Long> {

    public SequenceNumMdf findByIdempresa(Long idempresa);
}
