package br.com.conexao.mdfe.api.resource.documento;

import br.com.conexao.mdfe.api.model.mdfe.DAMdfeEnvioEmail;
import br.com.conexao.mdfe.api.service.documento.DocDAMDFEService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;

@RestController
@RequestMapping("/documentos")
public class DocumentoResource {

    @Autowired
    private DocDAMDFEService docDAMDFEService;

    @GetMapping("/damdfe/{idmdfe}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_DAMDFE') and #oauth2.hasScope('read')")
    public ResponseEntity<byte[]> gerarDAMDFE(@PathVariable Long idmdfe) throws Exception {
        docDAMDFEService.mdfeValida(idmdfe);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(docDAMDFEService.gerarBytesDAMDFE(idmdfe));
    }

    @PostMapping("/damdfe/email")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_DAMDFE') and #oauth2.hasScope('read')")
    public ResponseEntity gerarDAMDFEnviarEmail(@Valid @RequestBody DAMdfeEnvioEmail daMdfeEnvioEmail) throws SQLException, JRException {
        docDAMDFEService.mdfeValida(daMdfeEnvioEmail.getIdmdfe());

        docDAMDFEService.validarEmails(daMdfeEnvioEmail.getEmail());

        docDAMDFEService.gerarPDFEnviarEmail(daMdfeEnvioEmail.getIdmdfe(), daMdfeEnvioEmail.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body("Documento auxiliar de MDF-e gerado com sucesso! E-mail sendo enviado para o endereço: " +daMdfeEnvioEmail.getEmail());
    }


}
