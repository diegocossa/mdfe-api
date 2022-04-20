package br.com.conexao.mdfe.api.resource.motorista;

import br.com.conexao.mdfe.api.event.RecursoCriadoEvent;
import br.com.conexao.mdfe.api.model.motorista.Motorista;
import br.com.conexao.mdfe.api.model.motorista.MotoristaProjection;
import br.com.conexao.mdfe.api.repository.motorista.MotoristaRepository;
import br.com.conexao.mdfe.api.service.motorista.MotoristaService;
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
@RequestMapping("/motoristas")
public class MotoristaResource {

    @Autowired
    private MotoristaService motoristaService;

    @Autowired
    private MotoristaRepository motoristaRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping(params = "filter")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_MOTORISTA') and #oauth2.hasScope('read')")
    public Page<Motorista> filtrar(MotoristaProjection motoristaProjection, Pageable pageable) {
        return this.motoristaRepository.filter(motoristaProjection, pageable);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_MOTORISTA') and #oauth2.hasScope('read')")
    public ResponseEntity<List<Motorista>> listar() {
        List<Motorista> motoristas = motoristaService.findAll();
        return !motoristas.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(motoristas) : ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MOTORISTA') and #oauth2.hasScope('write')")
    public ResponseEntity<Motorista> cadastrar(@Valid @RequestBody Motorista motorista, HttpServletResponse response) {

        Motorista motoristaSalvo = motoristaService.salvar(motorista);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, motoristaSalvo.getIdmotorista()));
        return ResponseEntity.status(HttpStatus.CREATED).body(motoristaSalvo);
    }

    @PutMapping("/{idmotorista}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MOTORISTA') and #oauth2.hasScope('write')")
    public ResponseEntity<Motorista> atualizar(@PathVariable Long idmotorista, @Valid @RequestBody Motorista motorista) {

        Motorista motoristaSalvo = motoristaService.atualizar(idmotorista, motorista);

        return ResponseEntity.status(HttpStatus.OK).body(motoristaSalvo);
    }

    @PutMapping("/{idmotorista}/ativo")
    @PreAuthorize("hasAuthority('CADASTRAR_MOTORISTAS')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarPropriedadeFlagAtivo(@PathVariable Long idmotorista, @RequestBody Boolean flagativo) {
        motoristaService.atualizarPropriedadeFlagAtivo(idmotorista, flagativo);
    }

    @GetMapping("/{idmotorista}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_MOTORISTA') and #oauth2.hasScope('read')")
    public ResponseEntity<Motorista> buscarPeloId(@PathVariable Long idmotorista) {
        Motorista motorista = motoristaService.findByIdMotorista(idmotorista);
        return motorista != null ? ResponseEntity.ok(motorista) : ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idmotorista}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MOTORISTA') and #oauth2.hasScope('write')")
    public void delete(@PathVariable Long idmotorista) {
        motoristaService.deletar(idmotorista);
    }
}
