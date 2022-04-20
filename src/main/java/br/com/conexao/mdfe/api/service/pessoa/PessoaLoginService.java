package br.com.conexao.mdfe.api.service.pessoa;

import br.com.conexao.mdfe.api.model.pessoa.PessoaLogin;
import br.com.conexao.mdfe.api.repository.pessoa.PessoaLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessoaLoginService {

    @Autowired
    private PessoaLoginRepository pessoaLoginRepository;

    public PessoaLogin findByEmail(String email) {

        return pessoaLoginRepository.findByEmail(email);
    }

    public PessoaLogin findByIdpessoa(Long idpessoa) {

        return pessoaLoginRepository.findByIdpessoa(idpessoa);
    }

    @Transactional
    public PessoaLogin salvar(PessoaLogin pessoaLogin) {

        return pessoaLoginRepository.save(pessoaLogin);
    }

    @Transactional
    public void deletar(Long idpessoa) {
        pessoaLoginRepository.delete(idpessoa);
    }
}
