package br.com.conexao.mdfe.api.resource.mdfe;

import br.com.conexao.mdfe.api.event.RecursoCriadoEvent;
import br.com.conexao.mdfe.api.model.empresa.Empresa;
import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.model.mdfe.SituacaoMDFe;
import br.com.conexao.mdfe.api.model.veiculo.Veiculo;
import br.com.conexao.mdfe.api.repository.mdfe.MDFeRepository;
import br.com.conexao.mdfe.api.repository.mdfe.filter.MdfeFilter;
import br.com.conexao.mdfe.api.repository.mdfe.projection.MDFeProjection;
import br.com.conexao.mdfe.api.service.empresa.EmpresaService;
import br.com.conexao.mdfe.api.service.mdfe.MdfeLancamentoService;
import br.com.conexao.mdfe.api.service.motorista.MotoristaService;
import br.com.conexao.mdfe.api.service.sefaz.mdfe.execute.EnvioMdfe;
import br.com.conexao.mdfe.api.service.seguradora.SeguradoraService;
import br.com.conexao.mdfe.api.service.veiculo.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/mdfes")
public class MdfeLancamentoResource {

    @Autowired
    private MdfeLancamentoService mdfeLancamentoService;

    @Autowired
    private MDFeRepository MDFeRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private EnvioMdfe envioMdfe;

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private SeguradoraService seguradoraService;

    @Autowired
    private MotoristaService motoristaService;

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_MDFE') and #oauth2.hasScope('read')")
    public Page<MDFe> getMdfe(MdfeFilter mdfeFilter, Pageable pageable) {
        return MDFeRepository.filter(mdfeFilter, pageable);
    }

    @GetMapping(params = "resume")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_MDFE') and #oauth2.hasScope('read')")
    public Page<MDFeProjection> filtrar(MdfeFilter mdfeFilter, Pageable pageable, HttpServletRequest request) {

        if (request.getHeader("EMP") != null) {
            Empresa empresaFiltro = empresaService.findByIdEmpresa(Long.parseLong(request.getHeader("EMP")));

            if (empresaFiltro != null) {
                mdfeFilter.setAmbienteenvio(empresaFiltro.getTipoambiente().getDescricao());
            }
        }

        return this.MDFeRepository.resume(mdfeFilter, pageable);
    }

    @GetMapping("/{idmdfe}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_MDFE') and #oauth2.hasScope('read')")
    public ResponseEntity getMdfePorId(@PathVariable Long idmdfe) {
        MDFe mdfe = mdfeLancamentoService.findByIdMdfe(idmdfe);

        return !isNull(mdfe) ? ResponseEntity.status(HttpStatus.OK).body(mdfe) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/downloadxml/{idmdfe}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_MDFE') and #oauth2.hasScope('read')")
    public ResponseEntity<byte[]> downloadXMLPorIdMdfe(@PathVariable Long idmdfe) throws IOException, ParserConfigurationException, SAXException {
        MDFe mdfe = mdfeLancamentoService.findByIdMdfe(idmdfe);

        return nonNull(mdfe) ? ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                .body(mdfe.getXml()) : ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity<MDFe> cadastrar(@Valid @RequestBody MDFe MDFe, HttpServletResponse response) {
        // carrega objetos através do id, se não fizer isso ocorre detached.
        Veiculo veiculo = veiculoService.findByIdVeiculo(MDFe.getVeiculotracao().getIdveiculo());
        MDFe.setVeiculotracao(veiculo);

        if (MDFe.getSeglist() != null) {
            MDFe.getSeglist().forEach(seg -> {
                seg.setSeguradora(seguradoraService.findByIdSeguradora(seg.getSeguradora().getIdSeguradora()));
            });
        }

        if (MDFe.getCondutorlist() != null) {
            MDFe.getCondutorlist().forEach(condutor -> {
                condutor.setMotorista(motoristaService.findByIdMotorista(condutor.getMotorista().getIdmotorista()));
            });
        }

        if (MDFe.getVeiculoreboquelist() != null) {
            MDFe.getVeiculoreboquelist().forEach(veiculoReboque -> {
                veiculoReboque.setVeiculo(veiculoService.findByIdVeiculo(veiculoReboque.getVeiculo().getIdveiculo()));
            });
        }

        MDFe.setSituacao(SituacaoMDFe.GRAVADO);

        MDFe MDFeSalvo = mdfeLancamentoService.salvar(MDFe);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, MDFeSalvo.getIdmdfe()));

        return ResponseEntity.status(HttpStatus.CREATED).body(MDFe);
    }

    @PutMapping("/{idmdfe}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity<MDFe> atualizar(@PathVariable Long idmdfe, @Valid @RequestBody MDFe mdfeLancamento) {
        MDFe mdfeLancamentoSalvo = mdfeLancamentoService.atualizar(idmdfe, mdfeLancamento);
        return ResponseEntity.status(HttpStatus.OK).body(mdfeLancamentoSalvo);
    }

    @DeleteMapping("/{idmdfe}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long idmdfe) throws Exception {
        mdfeLancamentoService.deletar(idmdfe);
    }

    @PutMapping("/atualizarlote/{idmdfe}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity<MDFe> reenviarLote(@PathVariable Long idmdfe) {
        MDFe mdfeGravado = mdfeLancamentoService.findByIdMdfe(idmdfe);

        MDFe MDFeSalvo = mdfeLancamentoService.salvar(mdfeGravado);

        envioMdfe.enviar(MDFeSalvo);

        return ResponseEntity.status(HttpStatus.CREATED).body(MDFeSalvo);
    }

}
