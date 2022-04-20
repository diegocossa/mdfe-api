package br.com.conexao.mdfe.api.repository.senha;

import br.com.conexao.mdfe.api.model.pessoa.RecuperacaoSenha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecuperacaoSenhaRepository extends JpaRepository<RecuperacaoSenha, Long> {

    RecuperacaoSenha findByEmailIgnoreCase(String email);

    RecuperacaoSenha findByIdrecuperacaosenha(Long idrecuperacaosenha);
}
