package br.com.conexao.mdfe.api.resource.veiculo;

import br.com.conexao.mdfe.api.event.RecursoCriadoEvent;
import br.com.conexao.mdfe.api.model.veiculo.Veiculo;
import br.com.conexao.mdfe.api.model.veiculo.VeiculoProjection;
import br.com.conexao.mdfe.api.model.veiculo.VeiculoTipo;
import br.com.conexao.mdfe.api.repository.veiculo.VeiculoRepository;
import br.com.conexao.mdfe.api.service.veiculo.VeiculoService;
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
@RequestMapping("/veiculos")
public class VeiculoResource {
    
    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping(params = "filter")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_VEICULO') and #oauth2.hasScope('read')")
    public Page<Veiculo> filtrar(VeiculoProjection motoristaProjection, Pageable pageable){
        return this.veiculoRepository.filter(motoristaProjection, pageable);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_VEICULO') and #oauth2.hasScope('read')")
    public ResponseEntity listarVeiculos(){
        List<Veiculo> veiculos = veiculoService.findAll();
        return !veiculos.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(veiculos) : ResponseEntity.noContent().build();
    }

    @GetMapping("/tracao")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_VEICULO') and #oauth2.hasScope('read')")
    public ResponseEntity listarVeiculosTracao(){
        List<Veiculo> veiculos = veiculoService.findAllByVeiculoTipo(VeiculoTipo.TRACAO);
        return !veiculos.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(veiculos) : ResponseEntity.noContent().build();
    }

    @GetMapping("/reboque")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_VEICULO') and #oauth2.hasScope('read')")
    public ResponseEntity listarVeiculosReboque(){
        List<Veiculo> veiculos = veiculoService.findAllByVeiculoTipo(VeiculoTipo.REBOQUE);
        return !veiculos.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(veiculos) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{idveiculo}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_VEICULO') and #oauth2.hasScope('read')")
    public ResponseEntity buscarPeloId(@PathVariable Long idveiculo){
        Veiculo veiculo = veiculoService.findByIdVeiculo(idveiculo);
        return veiculo != null ? ResponseEntity.ok(veiculo) : ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_VEICULO') and #oauth2.hasScope('write')")
    public ResponseEntity<Veiculo> cadastrar(@Valid @RequestBody Veiculo veiculo, HttpServletResponse response) {
        Veiculo veiculoSalvo = veiculoService.salvar(veiculo);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, veiculoSalvo.getIdveiculo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(veiculoSalvo);
    }

    @PutMapping("/{idveiculo}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_VEICULO') and #oauth2.hasScope('write')")
    public ResponseEntity<Veiculo> atualizar(@PathVariable Long idveiculo, @Valid @RequestBody Veiculo veiculo){
        Veiculo veiculoSalvo = veiculoService.atualizar(idveiculo,veiculo);
        return ResponseEntity.status(HttpStatus.OK).body(veiculoSalvo);
    }

    @DeleteMapping("/{idveiculo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_VEICULO') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long idveiculo){
        veiculoService.deletar(idveiculo);
    }
}
