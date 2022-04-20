package br.com.conexao.mdfe.api.resource.pessoa;

import br.com.conexao.mdfe.api.event.RecursoCriadoEvent;
import br.com.conexao.mdfe.api.model.pessoa.Pessoa;
import br.com.conexao.mdfe.api.model.pessoa.TipoUsuario;
import br.com.conexao.mdfe.api.service.pessoa.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pessoa")
public class PessoaResource {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_PESSOA') and #oauth2.hasScope('read')")
    private ResponseEntity listarPessoas(){
        List<Pessoa> pessoas = pessoaService.findAll();

        return !pessoas.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(pessoas) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{idpessoa}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_PESSOA') and #oauth2.hasScope('read')")
    public ResponseEntity buscarPeloId(@PathVariable Long idpessoa){
        Pessoa pessoa = pessoaService.findByIdPessoa(idpessoa);
        return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
    public ResponseEntity<Pessoa> cadastrar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {

        //Setado fixo operador pois no cadastro de usuário sempre será operador, o motorista será inserido pelo
        //pelo cadastro do motorista.
        pessoa.setTipousuario(TipoUsuario.OPERADOR);

        Pessoa pessoaSalva = pessoaService.salvar(pessoa);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getIdpessoa()));
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @PutMapping("/{idpessoa}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long idpessoa, @Valid @RequestBody Pessoa pessoa){
        Pessoa pessoaSalva = pessoaService.atualizar(idpessoa,pessoa);
        return ResponseEntity.status(HttpStatus.OK).body(pessoaSalva);
    }

    @DeleteMapping("/{idpessoa}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long idpessoa){
        pessoaService.deletar(idpessoa);
    }
}
