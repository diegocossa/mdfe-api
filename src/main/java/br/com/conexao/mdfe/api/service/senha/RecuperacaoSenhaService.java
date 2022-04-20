package br.com.conexao.mdfe.api.service.senha;

import br.com.conexao.mdfe.api.async.AsyncEmailComponent;
import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.pessoa.PessoaLogin;
import br.com.conexao.mdfe.api.model.pessoa.RecuperacaoSenha;
import br.com.conexao.mdfe.api.repository.senha.RecuperacaoSenhaRepository;
import br.com.conexao.mdfe.api.resource.usuario.NovaSenha;
import br.com.conexao.mdfe.api.service.pessoa.PessoaLoginService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class RecuperacaoSenhaService {

    @Value("${chave.resetar.senha.hash}")
    private String chave;

    @Autowired
    private PessoaLoginService pessoaLoginService;

    @Autowired
    private RecuperacaoSenhaRepository recuperacaoSenhaRepository;

    @Autowired
    private AsyncEmailComponent asyncEmailComponent;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ValidaErro validaErro;

    @Transactional
    public void recuperarSenha(String email) throws Exception {

        RecuperacaoSenha recuperacaoSenha;
        PessoaLogin pessoaLogin = pessoaLoginService.findByEmail(email);

        if (pessoaLogin != null) {

            deletaRequisicaoAnterior(email);

            recuperacaoSenha = new RecuperacaoSenha();
            recuperacaoSenha.setEmail(pessoaLogin.getEmail());
            recuperacaoSenha.setTenantid(pessoaLogin.getTenantid());//schema
            recuperacaoSenha.setHash("");

            //salva logo abaixo salva novamente.
            recuperacaoSenhaRepository.saveAndFlush(recuperacaoSenha);

            //pega o id gerado e cria uma chave com ele.
            recuperacaoSenha.setHash(getHashIdChave(recuperacaoSenha));
            recuperacaoSenhaRepository.save(recuperacaoSenha);

            enviaEmail(recuperacaoSenha);

        } else {
            throw new Exception("E-mail não encontrado.");
        }
    }

    @Transactional
    public void salvaNovaSenha(NovaSenha novaSenha) {

        //verifica se uma solicitação de recuperação para o email.
        RecuperacaoSenha recuperacaoSenha = findByIdrecuperacaosenha(novaSenha.getId());

        //validações de senha
        validacoesSenha(novaSenha.getSenha(), novaSenha.getSenhaConfirmacao());

        validaLink(novaSenha.getChk(), recuperacaoSenha);

        //busca novamente pois o tenant não é passado no html por segurança.
        RecuperacaoSenha recuperacaoSenhaTenant = recuperacaoSenhaRepository.findByIdrecuperacaosenha(recuperacaoSenha.getIdrecuperacaosenha());
        if (recuperacaoSenha != null) {

            recuperacaoSenha.setTenantid(recuperacaoSenhaTenant.getTenantid());
            recuperacaoSenha.setEmail(recuperacaoSenhaTenant.getEmail());

            Query query = entityManager.createNativeQuery("update " + recuperacaoSenha.getTenantid() + ".pessoa set senhausuario = :senha  where email = :email");
            query.setParameter("senha", getHashSenha(novaSenha.getSenha()));
            query.setParameter("email", recuperacaoSenha.getEmail());
            query.executeUpdate();
            entityManager.close();

            recuperacaoSenhaRepository.delete(recuperacaoSenha.getIdrecuperacaosenha());
        }
    }

    private void deletaRequisicaoAnterior(String email) {
        RecuperacaoSenha recuperacaoSenha = recuperacaoSenhaRepository.findByEmailIgnoreCase(email);

        if (recuperacaoSenha != null) {
            recuperacaoSenhaRepository.delete(recuperacaoSenha.getIdrecuperacaosenha());
        }
    }

    private void enviaEmail(RecuperacaoSenha recuperacaoSenha) {
        asyncEmailComponent.asyncEnviarEmailRecuperacaoSenha(recuperacaoSenha);
    }

    private String getHashIdChave(RecuperacaoSenha recuperacaoSenha) {
        return DigestUtils.sha256Hex(recuperacaoSenha.getIdrecuperacaosenha() + chave);
    }

    //chaveValidar vem do link de recuperação enviados por email
    private void validaLink(String chaveValidar, RecuperacaoSenha recuperacaoSenha) {

        if (recuperacaoSenha != null) {

            verificaValidadeLink(recuperacaoSenha);

            //valida a chave aqui pois o codigo id pode ser mudado na url
            if (!recuperacaoSenha.getHash().equals(chaveValidar) && !chaveValidar.equals(getHashIdChave(recuperacaoSenha))) {
                disparaException("link.invalido.solicite.um.novo.link", "Link inválido, solicite um novo link na tela de login.");
            }
        }
    }

    private void disparaException(String chaveErroMessageProperties, String msgDesenvolvedor) {
        validaErro.addErro(chaveErroMessageProperties, msgDesenvolvedor);
        throw new MdfeException(validaErro);
    }

    private void verificaValidadeLink(RecuperacaoSenha recuperacaoSenha) {

        //se for maior que 1 o link está invalido, deve ter uma diferença de uma hora apenas.
        if (ChronoUnit.HOURS.between(recuperacaoSenha.getDthorasolicitacao(), LocalDateTime.now()) > 1) {
            disparaException("link.invalido.solicite.um.novo.link", "Link inválido, solicite um novo link na tela de login.");
        }
    }

    private RecuperacaoSenha findByIdrecuperacaosenha(String id) {
        return recuperacaoSenhaRepository.findByIdrecuperacaosenha(Long.parseLong(id));
    }

    private void validacoesSenha(String senhaUsuario, String senhaUsuarioConfirmacao) {
        validaTamanhoSenha(senhaUsuario);
        validarConfirmaSenha(senhaUsuario, senhaUsuarioConfirmacao);
    }

    private String getHashSenha(String senha) {
        return new BCryptPasswordEncoder().encode(senha);
    }

    private void validarConfirmaSenha(String senha, String confirmaSenha) {

        if (senha != null) {
            if (!senha.equals(confirmaSenha)) {
                disparaException("campo.senha.confirmacao.senha.nao.conferem", "Os campos de senha e confirmação de senha não conferem.");
            }
        }
    }

    private void validaTamanhoSenha(String senha) {

        if ((senha.length() < 6 || senha.length() > 20)) {
            disparaException("senha.deve.conter.entre.6.20.caracteres", "A senha deve conter entre 6 a 20 caracteres.");
        }
    }
}
