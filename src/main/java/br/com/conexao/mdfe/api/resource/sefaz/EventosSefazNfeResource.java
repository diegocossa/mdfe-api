package br.com.conexao.mdfe.api.resource.sefaz;

import br.com.conexao.mdfe.api.service.sefaz.nfe.execute.ConsultaStatusSefazNfe;
import br.com.conexao.mdfe.api.service.sefaz.nfe.execute.ConsultarNotaNfe;
import br.com.conexao.mdfe.api.service.sefaz.nfe.execute.DownloadNfe;
import br.com.conexao.mdfe.api.service.sefaz.nfe.execute.NfeDadosDownload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/eventos/nfe")
public class EventosSefazNfeResource {

    @Autowired
    private ConsultaStatusSefazNfe consultaStatusSefazNfe;

    @Autowired
    private ConsultarNotaNfe consultarNotaNfe;

    @Autowired
    private DownloadNfe downloadNfe;

    @GetMapping("/consultar/nota/{chave}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity consultarNfe(@PathVariable String chave){

        Map<String, String> retornoConsulta = consultarNotaNfe.consultar(chave);

        return ResponseEntity.ok(retornoConsulta);
    }

    @GetMapping("/consultar/status")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity consultarStatusSefaz(){
        Map<String, String> retornoConsulta = consultaStatusSefazNfe.consultar();

        return ResponseEntity.ok(retornoConsulta);
    }

    @PostMapping("/download")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_MDFE') and #oauth2.hasScope('write')")
    public ResponseEntity downloadNfe(@RequestBody NfeDadosDownload nfeDadosDownload) {
        Map<String, String> retornoConsulta = downloadNfe.download(nfeDadosDownload.getCnpj(), nfeDadosDownload.getChaveNfe());

        return ResponseEntity.ok(retornoConsulta);
    }
}
