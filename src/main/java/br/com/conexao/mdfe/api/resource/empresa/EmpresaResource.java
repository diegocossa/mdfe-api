package br.com.conexao.mdfe.api.resource.empresa;

import br.com.conexao.mdfe.api.event.RecursoCriadoEvent;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.empresa.Empresa;
import br.com.conexao.mdfe.api.model.empresa.EmpresaProjection;
import br.com.conexao.mdfe.api.repository.empresa.EmpresaRepository;
import br.com.conexao.mdfe.api.service.empresa.EmpresaService;
import br.com.conexao.mdfe.api.service.empresa.SequenceNumMdfService;
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
import java.math.BigInteger;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/empresas")
public class EmpresaResource {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private SequenceNumMdfService sequenceNumMdfService;

    @Autowired
    private ValidaErro validaErro;

    @GetMapping(params = "filter")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_EMPRESA') and #oauth2.hasScope('read')")
    public Page<Empresa> filterEmpresas(EmpresaProjection empresaProjection, Pageable pageable) {
        return empresaRepository.filter(empresaProjection, pageable);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_EMPRESA') and #oauth2.hasScope('read')")
    public ResponseEntity listarEmpresas(){
        List<Empresa> empresas = empresaService.findAll();
        return !empresas.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(empresas) : ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_EMPRESA') and #oauth2.hasScope('write')")
    public ResponseEntity cadastrar(@Valid @RequestBody Empresa empresa, HttpServletResponse response){
        Empresa empresaSalva = empresaService.salvar(empresa);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, empresaSalva.getIdempresa()));
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaSalva);
    }

    @PutMapping("/{idempresa}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_EMPRESA') and #oauth2.hasScope('write')")
    public ResponseEntity<Empresa> atualizar(@PathVariable Long idempresa, @Valid @RequestBody Empresa empresa){
        Empresa empresaSalva = empresaService.atualizar(idempresa,empresa);
        return ResponseEntity.status(HttpStatus.OK).body(empresaSalva);
    }

    @GetMapping("/{idempresa}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_EMPRESA') and #oauth2.hasScope('read')")
    public ResponseEntity<Empresa> buscarPeloId(@PathVariable Long idempresa){
        Empresa empresa = empresaService.findByIdEmpresa(idempresa);
        return empresa != null ? ResponseEntity.ok(empresa) : ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idempresa}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_EMPRESA') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long idempresa){
        empresaService.deletar(idempresa);
    }

    @PostMapping("/atualizar/sequence/{idempresa}/{valor}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_EMPRESA') and #oauth2.hasScope('write')")
    public void alterarValorSequenceNumMdf(@PathVariable Long idempresa, @PathVariable Long valor){

        if (nonNull(idempresa) && nonNull(valor)) {
            Empresa empresa = empresaService.findByIdEmpresa(idempresa);
            if (isNull(empresa)) {
                validaErro.addErro("empresa.invalida","A empresa indicada não esta cadastrada na base de dados.");
                validaErro.trataErros();
            }

            sequenceNumMdfService.atualizarSequence(idempresa, valor);
        } else
        {
            validaErro.addErro("erro-atualizar-sequence","É obrigatorio o envio de um código de empresa e um valor para a sequence.");
            validaErro.trataErros();
        }
    }

    @GetMapping("/contador/numeroManifesto/{idempresa}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_EMPRESA') and #oauth2.hasScope('read')")
    public ResponseEntity<BigInteger> buscarContadorNumeroManifesto(@PathVariable Long idempresa){
        return ResponseEntity.ok(sequenceNumMdfService.getValorAtualSequence(idempresa));
    }

}
