package br.com.conexao.mdfe.api.resource.usuario;

import br.com.conexao.mdfe.api.event.RecursoCriadoEvent;
import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.grupo.GrupoEnum;
import br.com.conexao.mdfe.api.model.pessoa.*;
import br.com.conexao.mdfe.api.repository.pessoa.PessoaRepository;
import br.com.conexao.mdfe.api.service.pessoa.PessoaLoginService;
import br.com.conexao.mdfe.api.service.pessoa.PessoaService;
import br.com.conexao.mdfe.api.service.senha.RecuperacaoSenhaService;
import br.com.conexao.mdfe.api.tenant.TenantContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioResource {

    @Autowired
    private RecuperacaoSenhaService recuperacaoSenhaService;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PessoaLoginService pessoaLoginService;

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/reset/senha")
    public ResponseEntity recuperar(@RequestBody EmailResetarSenha emailResetarSenha) {

        try {
            //envia um email parar recuperar senha
            recuperacaoSenhaService.recuperarSenha(emailResetarSenha.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("E-mail não encontrado.");
        }
    }

    @PostMapping("/nova/senha")
    public ResponseEntity novaSenha(@RequestBody NovaSenha novaSenha) {

        recuperacaoSenhaService.salvaNovaSenha(novaSenha);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and #oauth2.hasScope('write')")
    @Transactional
    public ResponseEntity<Usuario> cadastrar(@Valid @RequestBody Usuario usuario, HttpServletResponse response) {

        String senha = GeradorSenha.gerar(6);

        //Cria sempre no grupo de operadores
        RetornoCadastroUsuario retorno = pessoaService.cadastrarNovoUsuario(usuario, TenantContext.getCurrentTenant(), senha, GrupoEnum.OPERADORES);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, retorno.getIdpessoa()));

        pessoaService.enviarEmailCadastroUsuario(retorno.getSenha(), retorno.getPessoa());

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping("/{idusuario}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_USUARIO') and #oauth2.hasScope('read')")
    public ResponseEntity buscarPeloId(@PathVariable Long idusuario) {

        Pessoa pessoa = pessoaService.findByIdPessoa(idusuario);

        Usuario usuario = new Usuario();

        BeanUtils.copyProperties(pessoa, usuario);

        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.noContent().build();
    }

    @GetMapping(params = "filter")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_USUARIO') and #oauth2.hasScope('read')")
    public Page<Pessoa> filtrar(Pessoa pessoa, Pageable pageable) {
        return this.pessoaRepository.filter(pessoa, pageable);
    }

    @GetMapping(params = "filterOnPublic")
    @PreAuthorize("hasAuthority('ROLE_PAINEL_ADM') and #oauth2.hasScope('read')")
    public Page<PessoaPublic> filtrarOnPublic(Pessoa pessoa, Pageable pageable) {
        return this.pessoaRepository.filterOnPublic(pessoa, pageable);
    }

    @PutMapping("/{idusuario}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and #oauth2.hasScope('write')")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long idusuario, @Valid @RequestBody Usuario usuario) {

        Pessoa pessoa = pessoaService.findByIdpessoa(idusuario);
        PessoaLogin pessoaLogin = pessoaLoginService.findByEmail(pessoa.getEmail());

        BeanUtils.copyProperties(usuario, pessoa, "idpessoa", "idpessoatenant", "tipousuario", "tenantid");
        BeanUtils.copyProperties(pessoa, pessoaLogin, "idpessoa", "idpessoatenant", "tipousuario", "tenantid");

        pessoaService.salvar(pessoa);
        pessoaLoginService.salvar(pessoaLogin);

        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }

    @PutMapping("alterarSenha")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and #oauth2.hasScope('write')")
    public ResponseEntity trocarSenha(@Valid @RequestBody AlterarSenha alterarSenha) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Pessoa pessoa = pessoaService.findByIdpessoa(alterarSenha.getIdUsuario());

        if (encoder.matches(alterarSenha.getSenhaAtual(), pessoa.getSenhausuario())) {
            PessoaLogin pessoaLogin = pessoaLoginService.findByEmail(pessoa.getEmail());

            pessoa.setSenhausuario(encoder.encode(alterarSenha.getNovaSenha()));
            pessoaLogin.setSenhausuario(encoder.encode(alterarSenha.getNovaSenha()));

            pessoaService.salvar(pessoa);
            pessoaLoginService.salvar(pessoaLogin);
        } else {
            validaErro.addErro("senha-usuario-invalida", "Senha anterior não bate com a senha cadastrada no banco de dados.");
            throw new MdfeException(validaErro);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{idusuario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO') and #oauth2.hasScope('write')")
    public void delete(@PathVariable Long idusuario) {

        Pessoa pessoa = pessoaService.findByIdpessoa(idusuario);
        pessoa.setFlagdel(true);
        pessoa.setFlagativo(false);
        pessoaService.salvar(pessoa);

        PessoaLogin pessoaLogin = pessoaLoginService.findByEmail(pessoa.getEmail());

        if (pessoaLogin != null) {
            pessoaLogin.setFlagdel(true);
            pessoaLogin.setFlagativo(false);
            pessoaLoginService.salvar(pessoaLogin);
        } else {
            validaErro.addErro("nao.foi.possivel.excluir.usuario", "o.usuario.deletado.nao.foi.encontrado.no.schema.public");
            if (validaErro.hasError()) {
                throw new MdfeException(validaErro);
            }
        }
    }
}
