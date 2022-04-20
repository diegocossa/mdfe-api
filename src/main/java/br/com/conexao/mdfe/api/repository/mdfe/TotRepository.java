package br.com.conexao.mdfe.api.repository.mdfe;

import br.com.conexao.mdfe.api.model.mdfe.generica.Totalizadores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TotRepository extends JpaRepository<Totalizadores, Long> {

}
