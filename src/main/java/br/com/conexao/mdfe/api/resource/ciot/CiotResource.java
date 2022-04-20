package br.com.conexao.mdfe.api.resource.ciot;

import br.com.conexao.mdfe.api.event.RecursoCriadoEvent;
import br.com.conexao.mdfe.api.model.ciot.Ciot;
import br.com.conexao.mdfe.api.repository.ciot.CiotRepository;
import br.com.conexao.mdfe.api.service.ciot.CiotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ciots")
public class CiotResource {

    @Autowired
    private CiotService ciotService;

    @Autowired
    private CiotRepository ciotRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping(params = "filter")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_CIOT') and #oauth2.hasScope('read')")
    public Page<Ciot> filterCiots(Ciot ciot, Pageable pageable) {
        return ciotRepository.filter(ciot, pageable);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_CIOT') and #oauth2.hasScope('read')")
    public ResponseEntity listarCiots(){
        List<Ciot> ciots = ciotService.findAll();
        return !ciots.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(ciots) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{idciot}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_CIOT') and #oauth2.hasScope('read')")
    public ResponseEntity buscarPeloId(@PathVariable Long idciot){
        Ciot ciot = ciotService.findByIdCiot(idciot);
        return ciot != null ? ResponseEntity.ok(ciot) : ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CIOT') and #oauth2.hasScope('write')")
    public ResponseEntity<Ciot> cadastrar(@Valid @RequestBody Ciot ciot, HttpServletResponse response) {
        Ciot ciotSalvo = ciotService.salvar(ciot);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, ciotSalvo.getIdciot()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ciotSalvo);
    }

    @PutMapping("/{idciot}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CIOT') and #oauth2.hasScope('write')")
    public ResponseEntity<Ciot> atualizar(@PathVariable Long idciot, @Valid @RequestBody Ciot ciot){
        Ciot ciotSalvo = ciotService.atualizar(idciot,ciot);
        return ResponseEntity.status(HttpStatus.OK).body(ciotSalvo);
    }

    @DeleteMapping("/{idciot}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CIOT') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long idciot){
        ciotService.deletar(idciot);
    }

    @GetMapping("/buscar/empresa/{idempresa}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_CIOT') and #oauth2.hasScope('read')")
    public ResponseEntity buscarPeloIdEmpresa(@PathVariable Long idempresa){
        Ciot ciot = ciotService.buscarPelaEmpresa(idempresa);
        return ciot != null ? ResponseEntity.ok(ciot) : ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/motorista/{idmotorista}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_CIOT') and #oauth2.hasScope('read')")
    public ResponseEntity buscarPeloIdMotorista(@PathVariable Long idmotorista){
        Ciot ciot = ciotService.buscarPeloMotorista(idmotorista);
        return ciot != null ? ResponseEntity.ok(ciot) : ResponseEntity.noContent().build();
    }
}
