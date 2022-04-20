package br.com.conexao.mdfe.api.repository.mdfe;

import br.com.conexao.mdfe.api.model.mdfe.generica.Periculosidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriRepository extends JpaRepository<Periculosidade, Long> {

}
