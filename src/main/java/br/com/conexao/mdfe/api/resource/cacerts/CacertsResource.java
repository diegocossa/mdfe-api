package br.com.conexao.mdfe.api.resource.cacerts;

import br.com.conexao.mdfe.api.async.GeradorCadeiaCertificadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cacerts")
public class CacertsResource {

    @Autowired
    private GeradorCadeiaCertificadoService geradorCadeiaCertificado;

    @GetMapping("/atualizar")
    @PreAuthorize("hasAuthority('ROLE_ATUALIZAR_CACERTS') and #oauth2.hasScope('write')")
    public ResponseEntity atualizarCacerts(){
        try
        {
            geradorCadeiaCertificado.gerarCadeiaProducao();
            geradorCadeiaCertificado.gerarCadeiaHomologacao();

            return ResponseEntity.ok("Arquivos de cacerts atualizados com sucesso!");

        } catch (Exception e){

            return ResponseEntity.ok("Erro ao atualizar cacerts. Exception: " + e.getMessage());

        }
    }

}
