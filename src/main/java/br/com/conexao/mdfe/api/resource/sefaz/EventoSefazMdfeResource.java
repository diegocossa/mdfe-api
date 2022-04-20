package br.com.conexao.mdfe.api.resource.sefaz;

import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.mdfe.CancelamentoMdfe;
import br.com.conexao.mdfe.api.model.mdfe.EncerramentoMdfe;
import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.service.mdfe.MdfeLancamentoService;
import br.com.conexao.mdfe.api.service.sefaz.EventoSefazService;
import br.com.conexao.mdfe.api.service.sefaz.mdfe.execute.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eventos/mdfe")
public class EventoSefazMdfeResource {

    @Autowired
    private EnvioMdfe envioMdfe;

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private MdfeLancamentoService mdfeLancamentoService;

    @Autowired
    private ConsultaRecibo consultaRecibo;

    @Autowired
    private ConsultaStatusSefazMdfe consultaStatusSefazMdfe;

    @Autowired
    private EventoSefazService eventoSefazService;

    @Autowired
    private CancelaMdfe cancelaMdfe;

    @Autowired
    private ConsultaNaoEncerrados consultaNaoEncerrados;

    @Autowired
    private EncerraMdfe encerraMdfe;

    @PutMapping("/enviar/{idmdfe}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity<MDFe> enviarMdfe(@PathVariable Long idmdfe, @Valid @RequestBody DatasEmissao datasEmissao) {

        MDFe rodov = mdfeLancamentoService.buscarMdfePeloId(idmdfe);

        rodov.setDhemi(datasEmissao.getDhemi());
        rodov.setDhiniviagem(datasEmissao.getDhiniviagem());

        return ResponseEntity.ok(envioMdfe.enviar(rodov));
    }

    @PostMapping("/cancelar/{idmdfe}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity<MDFe> cancelarMdfe(@PathVariable Long idmdfe, @Valid @RequestBody CancelamentoMdfe cancelamentoMdfe){
        MDFe rodov = mdfeLancamentoService.buscarMdfePeloId(idmdfe);
        cancelamentoMdfe.setChave(rodov.getChave());
        cancelamentoMdfe.setProtocolo(rodov.getNumprotocolo());
        Map<String, String> retornoConsulta = cancelaMdfe.cancelar(cancelamentoMdfe, rodov);

        return ResponseEntity.ok(rodov);
    }

    @PostMapping("/encerrar/{idmdfe}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity<MDFe> encerrarMdfe(@PathVariable Long idmdfe, @Valid @RequestBody EncerramentoMdfe encerramentoMdfe){
        MDFe rodov = mdfeLancamentoService.buscarMdfePeloId(idmdfe);
        encerramentoMdfe.setChave(rodov.getChave());
        encerramentoMdfe.setNumprotocolo(rodov.getNumprotocolo());

        Map<String, String> retornoConsulta = encerraMdfe.encerrar(encerramentoMdfe, rodov);

        return ResponseEntity.ok(rodov);
    }

    @PostMapping("/encerrarPelaChave")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity<Map<String, String>> encerrarMdfePelaChave(@Valid @RequestBody EncerramentoMdfe encerramentoMdfe){
        Map<String, String> retornoConsulta = encerraMdfe.encerrar(encerramentoMdfe, null);

        return ResponseEntity.ok(retornoConsulta);
    }

    @GetMapping("/consulta/naoencerrados/{cnpj}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity consultarMdfesNaoEncerrados(@PathVariable String cnpj){
        List<Map<String, String>> retornoConsulta = consultaNaoEncerrados.consultar(cnpj);

        return ResponseEntity.ok(retornoConsulta);
    }

    @GetMapping("/consultar/recibo/{idmdfe}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity<MDFe> consultarMdfePorRecibo(@PathVariable Long idmdfe){

        MDFe rodov = mdfeLancamentoService.buscarMdfePeloId(idmdfe);

        return ResponseEntity.ok(consultaRecibo.consultar(rodov));
    }

    @GetMapping("/consultar/status")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity consultarStatusSefaz(){
        Map<String, String> retornoConsulta = consultaStatusSefazMdfe.consultar();

        return ResponseEntity.ok(retornoConsulta);
    }

    @GetMapping("/incluir/condutor/{idmdfe}/{idmotorista}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity<MDFe> incluirCondutor(@PathVariable Long idmdfe, @PathVariable Long idmotorista){
        MDFe rodov = eventoSefazService.prepararCondutorEEnviarEvento(idmdfe, idmotorista);
        return ResponseEntity.ok(rodov);
    }
}
