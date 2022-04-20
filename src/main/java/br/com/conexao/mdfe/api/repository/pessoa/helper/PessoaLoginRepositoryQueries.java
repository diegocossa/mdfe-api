package br.com.conexao.mdfe.api.repository.pessoa.helper;

import br.com.conexao.mdfe.api.model.pessoa.PessoaLogin;

import java.util.List;
import java.util.Optional;

public interface PessoaLoginRepositoryQueries {

    public Optional<PessoaLogin> emailEAtivo(String email);

    public Optional<PessoaLogin> cpfEAtivo(String cpf);

    public List<String> permissoes(PessoaLogin pessoa);

}
