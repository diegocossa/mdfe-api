package br.com.conexao.mdfe.api.repository.proprietario;

import br.com.conexao.mdfe.api.model.proprietario.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProprietarioRepository extends JpaRepository<Proprietario, Long> {
}
