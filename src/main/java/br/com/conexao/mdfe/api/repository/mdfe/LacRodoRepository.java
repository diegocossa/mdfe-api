package br.com.conexao.mdfe.api.repository.mdfe;

import br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario.LacreRodoviario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LacRodoRepository extends JpaRepository<LacreRodoviario, Long> {

}
