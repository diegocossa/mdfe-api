package br.com.conexao.mdfe.api.resource.configuracao;

import br.com.conexao.mdfe.api.event.RecursoCriadoEvent;
import br.com.conexao.mdfe.api.model.configuracao.Configuracao;
import br.com.conexao.mdfe.api.service.configuracao.ConfiguracaoService;
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
@RequestMapping("/configuracao")
public class ConfiguracaoResource {

    @Autowired
    private ConfiguracaoService configuracaoService;

    @Autowired
    private ApplicationEventPublisher publisher;

/*    @GetMapping(params = "filter")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_CONFIGURACAO') and #oauth2.hasScope('read')")
    public Page<Configuracao> filterConfiguracaos(Configuracao configuracao, Pageable pageable) {
        return configuracaoService.filter(configuracao, pageable);
    }*/

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_CONFIGURACAO') and #oauth2.hasScope('read')")
    public ResponseEntity listarConfiguracoes(){
        List<Configuracao> configuracoes = configuracaoService.findAll();
        return !configuracoes.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(configuracoes) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{idconfiguracao}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_CONFIGURACAO') and #oauth2.hasScope('read')")
    public ResponseEntity buscarPeloId(@PathVariable Long idconfiguracao){
        Configuracao configuracao = configuracaoService.findByIdconfiguracao(idconfiguracao);
        return configuracao != null ? ResponseEntity.ok(configuracao) : ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CONFIGURACAO') and #oauth2.hasScope('write')")
    public ResponseEntity<Configuracao> cadastrar(@Valid @RequestBody Configuracao configuracao, HttpServletResponse response) {
        Configuracao configuracaoSalvo = configuracaoService.salvar(configuracao);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, configuracaoSalvo.getIdconfiguracao()));
        return ResponseEntity.status(HttpStatus.CREATED).body(configuracaoSalvo);
    }

    @PutMapping("/{idconfiguracao}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CONFIGURACAO') and #oauth2.hasScope('write')")
    public ResponseEntity<Configuracao> atualizar(@PathVariable Long idconfiguracao, @Valid @RequestBody Configuracao configuracao){
        Configuracao configuracaoSalvo = configuracaoService.atualizar(idconfiguracao,configuracao);
        return ResponseEntity.status(HttpStatus.OK).body(configuracaoSalvo);
    }

    @DeleteMapping("/{idconfiguracao}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CONFIGURACAO') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long idconfiguracao){
        configuracaoService.deletar(idconfiguracao);
    }

    @GetMapping("/buscar/empresa/{idempresa}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_CONFIGURACAO') and #oauth2.hasScope('read')")
    public ResponseEntity buscarPeloIdEmpresa(@PathVariable Long idempresa){
        List<Configuracao> configuracoes = configuracaoService.buscarPelaEmpresa(idempresa);
        return configuracoes != null ? ResponseEntity.ok(configuracoes) : ResponseEntity.noContent().build();
    }
}
