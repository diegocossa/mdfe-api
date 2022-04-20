package br.com.conexao.mdfe.api.service.pessoa;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.grupo.Grupo;
import br.com.conexao.mdfe.api.model.grupo.GrupoEnum;
import br.com.conexao.mdfe.api.model.pessoa.*;
import br.com.conexao.mdfe.api.repository.pessoa.PessoaRepository;
import br.com.conexao.mdfe.api.service.email.MailClientService;
import br.com.conexao.mdfe.api.service.grupo.GrupoService;
import br.com.conexao.mdfe.api.service.sefaz.Validacoes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class PessoaService {

    private static final Logger logger = LoggerFactory.getLogger(Validacoes.class);

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PessoaLoginService pessoaLoginService;

    @Autowired
    private MailClientService mailClientService;

    public Pessoa findByIdPessoa(Long idPessoa) {
        return pessoaRepository.findByIdpessoa(idPessoa);
    }

    public List<Pessoa> findAll() {
        return pessoaRepository.findAllByFlagdelIsFalse();
    }

    private Pessoa buscarPessoaPeloId(Long idpessoa) {
        Pessoa pessoaSalva = findByIdPessoa(idpessoa);
        if (isNull(pessoaSalva)) {
            throw new EmptyResultDataAccessException(1);
        }
        return pessoaSalva;
    }

    @Transactional
    public Pessoa salvar(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public void deletar(Long idpessoa) {
        Pessoa pessoa = buscarPessoaPeloId(idpessoa);

        //Se for do grupo administrador ou G3Brasil nao pode deletar o usuário.
        if (pessoa.getGrupos().stream().filter(p -> p.getIdgrupo() == GrupoEnum.ADMINISTRADORES.getCodigo()).count() > 0
        || pessoa.getGrupos().stream().filter(p -> p.getIdgrupo() == GrupoEnum.G3BRASIL.getCodigo()).count() > 0) {
            validaErro.addErro("erro-deletar-administrador", "Erro ao tentar deletar um usuário do grupo administrador");
            throw new MdfeException(validaErro);
        }

        pessoa.setFlagdel(true);
        this.salvar(pessoa);
    }

    @Transactional
    public Pessoa atualizar(Long idpessoa, Pessoa pessoa) {
        Pessoa pessoaSalva = buscarPessoaPeloId(idpessoa);
        BeanUtils.copyProperties(pessoa, pessoaSalva, "idpessoa", "idpessoatenant", "tipousuario", "tenantid");
        return pessoaSalva;
    }

    public Pessoa findByEmail(String email) {
        return pessoaRepository.findByEmail(email);
    }

    public Pessoa findByIdpessoa(Long idpessoa) {
        return pessoaRepository.findByIdpessoa(idpessoa);
    }

    @Transactional
    public RetornoCadastroUsuario cadastrarNovoUsuario(Usuario usuario, String tenantId, String senha, GrupoEnum grupoEnum){

        Pessoa pessoa = new Pessoa();

        pessoa.setCpf(usuario.getCpf());
        pessoa.setEmail(usuario.getEmail());
        pessoa.setFlagativo(usuario.getFlagativo());
        pessoa.setNome(usuario.getNome());
        pessoa.setTipousuario(TipoUsuario.OPERADOR);
        pessoa.setFone(usuario.getFone());
        pessoa.setIdempresapadrao(1l);
        pessoa.setTenantid(tenantId);
        pessoa.setSenhausuario(new BCryptPasswordEncoder().encode(senha));

        Grupo grupo = grupoService.findByIdGrupo(grupoEnum.getCodigo());
        pessoa.addGrupo(grupo);

        Pessoa pessoaSalvo = pessoaService.salvar(pessoa);

        if (pessoaSalvo.getIdpessoa() != null) {
            usuario.setIdpessoa(pessoaSalvo.getIdpessoa());
        }

        PessoaLogin pessoaLogin = new PessoaLogin();

        pessoaLogin.setIdpessoatenant(pessoaSalvo.getIdpessoa());

        BeanUtils.copyProperties(pessoaSalvo, pessoaLogin, "idpessoa");

        pessoaLoginService.salvar(pessoaLogin);//vai salvar no schema public

        RetornoCadastroUsuario retorno = new RetornoCadastroUsuario();

        retorno.setIdpessoa(pessoaSalvo.getIdpessoa());
        retorno.setPessoa(pessoa);
        retorno.setSenha(senha);

        return retorno;
    }

    public void enviarEmailCadastroUsuario(String senha, Pessoa pessoa) {
        try {
            mailClientService.prepareAndSendEmailDadosDeAcesso(pessoa.getEmail(), senha, pessoa.getNome());
        } catch (Exception e) {
            validaErro.addErro("erro.envio.email.dados.acesso", e.getMessage());
        } finally {
            if (validaErro.hasError()) {
                throw new MdfeException(validaErro);
            }
        }
    }
}
