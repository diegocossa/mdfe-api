package br.com.conexao.mdfe.api.resource.certificado;

import br.com.conexao.mdfe.api.event.RecursoCriadoEvent;
import br.com.conexao.mdfe.api.model.certificado.Certificado;
import br.com.conexao.mdfe.api.service.certificado.CertificadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/certificado")
public class CertificadoResource {

    @Autowired
    private CertificadoService certificadoService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CERTIFICADO') and #oauth2.hasScope('write')")
    public ResponseEntity<Certificado> cadastrar(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("senha") String senha,
                                                 HttpServletResponse response) throws Exception {
        Certificado certificadoSalvo = certificadoSalvo = certificadoService.salvar(file, senha);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, certificadoSalvo.getIdcertificado()));
        return ResponseEntity.status(HttpStatus.CREATED).body(certificadoSalvo);
    }

    @GetMapping("/{idcertificado}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_CERTIFICADO') and #oauth2.hasScope('read')")
    public ResponseEntity buscarPeloId(@PathVariable Long idcertificado) throws IOException {
        Certificado certificado = certificadoService.findByIdCertificado(idcertificado);
        return certificado != null ? ResponseEntity.ok(certificado) : ResponseEntity.noContent().build();
    }

    @GetMapping("/empresa/{idempresa}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_CERTIFICADO') and #oauth2.hasScope('read')")
    public ResponseEntity buscaPeloIdEmpresa(@PathVariable Long idempresa) throws IOException {
        Certificado certificado = certificadoService.findByIdempresa(idempresa);
        return certificado != null ? ResponseEntity.ok(certificado) : ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idcertificado}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CERTIFICADO') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long idcertificado) {
        certificadoService.deletar(idcertificado);
    }

}
