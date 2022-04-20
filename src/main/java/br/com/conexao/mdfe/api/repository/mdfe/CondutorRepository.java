package br.com.conexao.mdfe.api.repository.mdfe;

import br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario.Condutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CondutorRepository extends JpaRepository<Condutor, Long> {

}
