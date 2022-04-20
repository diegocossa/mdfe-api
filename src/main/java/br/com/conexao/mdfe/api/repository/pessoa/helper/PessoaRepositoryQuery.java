package br.com.conexao.mdfe.api.repository.pessoa.helper;

import br.com.conexao.mdfe.api.model.pessoa.Pessoa;
import br.com.conexao.mdfe.api.model.pessoa.PessoaPublic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PessoaRepositoryQuery {
    Page<Pessoa> filter(Pessoa pessoa, Pageable pageable);
    Page<PessoaPublic> filterOnPublic(Pessoa pessoa, Pageable pageable);
}
