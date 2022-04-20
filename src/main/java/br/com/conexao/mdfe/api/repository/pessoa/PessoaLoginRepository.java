package br.com.conexao.mdfe.api.repository.pessoa;


import br.com.conexao.mdfe.api.model.pessoa.PessoaLogin;
import br.com.conexao.mdfe.api.repository.pessoa.helper.PessoaLoginRepositoryQueries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaLoginRepository extends JpaRepository<PessoaLogin, Long>, PessoaLoginRepositoryQueries {

    PessoaLogin findByEmail(String email);

    PessoaLogin findByIdpessoa(Long idpessoa);

}
