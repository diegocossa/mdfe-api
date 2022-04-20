package br.com.conexao.mdfe.api.resource.seguradora;

import br.com.conexao.mdfe.api.event.RecursoCriadoEvent;
import br.com.conexao.mdfe.api.model.seguradora.Seguradora;
import br.com.conexao.mdfe.api.repository.seguradora.SeguradoraRepository;
import br.com.conexao.mdfe.api.service.seguradora.SeguradoraService;
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
@RequestMapping("/seguradoras")
public class SeguradoraResource {

    @Autowired
    private SeguradoraService seguradoraService;

    @Autowired
    private SeguradoraRepository seguradoraRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping(params = "filter")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_SEGURADORA') and #oauth2.hasScope('read')")
    public Page<Seguradora> filtrar(Seguradora seguradora, Pageable pageable){
        return this.seguradoraRepository.filter(seguradora, pageable);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_SEGURADORA') and #oauth2.hasScope('read')")
    public ResponseEntity getListaSeguradoras(){
        List<Seguradora> seguradoras = seguradoraService.findAll();
        return !seguradoras.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(seguradoras) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{idseguradora}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_SEGURADORA') and #oauth2.hasScope('read')")
    public ResponseEntity buscarPeloId(@PathVariable Long idseguradora){
        Seguradora seguradora = seguradoraService.findByIdSeguradora(idseguradora);
        return seguradora != null ? ResponseEntity.ok(seguradora) : ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_SEGURADORA') and #oauth2.hasScope('write')")
    public ResponseEntity<Seguradora> cadastrar(@Valid @RequestBody Seguradora seguradora, HttpServletResponse response) {
        Seguradora seguradoraSalva = seguradoraService.salvar(seguradora);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, seguradoraSalva.getIdSeguradora()));
        return ResponseEntity.status(HttpStatus.CREATED).body(seguradoraSalva);
    }

    @PutMapping("/{idseguradora}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_SEGURADORA') and #oauth2.hasScope('write')")
    public ResponseEntity<Seguradora> atualizar(@PathVariable Long idseguradora, @Valid @RequestBody Seguradora seguradora){
        Seguradora seguradoraSalva = seguradoraService.atualizar(idseguradora,seguradora);
        return ResponseEntity.status(HttpStatus.OK).body(seguradoraSalva);
    }

    @DeleteMapping("/{idseguradora}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_SEGURADORA') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long idseguradora){
        seguradoraService.deletar(idseguradora);
    }

}
