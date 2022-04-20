package br.com.conexao.mdfe.api.repository.pessoa;


import br.com.conexao.mdfe.api.model.pessoa.Pessoa;
import br.com.conexao.mdfe.api.repository.pessoa.helper.PessoaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery {

    Pessoa findByIdpessoa(Long idPessoa);

    List<Pessoa> findAllByFlagdelIsFalse();

    Pessoa findByEmail(String email);
}

