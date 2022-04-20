package br.com.conexao.mdfe.api.resource.mdfe;

import br.com.conexao.mdfe.api.model.empresa.Empresa;
import br.com.conexao.mdfe.api.repository.mdfe.filter.MdfeTotalizadorFilter;
import br.com.conexao.mdfe.api.service.empresa.EmpresaService;
import br.com.conexao.mdfe.api.service.mdfe.ConsultaTotalizadorService;
import com.fincatto.documentofiscal.DFAmbiente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/totalizadores")
public class MdfeTotalizadorResource {

    @Autowired
    private ConsultaTotalizadorService consultaTotalizadorService;

    @Autowired
    private EmpresaService empresaService;

    @GetMapping("/listatotais")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_MDFE') and #oauth2.hasScope('read')")
    public ResponseEntity getListaTotais(HttpServletRequest request) {
        String ambienteEnvio = DFAmbiente.PRODUCAO.getDescricao();
        if (request.getHeader("EMP") != null) {
            Empresa empresaFiltro = empresaService.findByIdEmpresa(Long.parseLong(request.getHeader("EMP")));

            if (empresaFiltro != null) {
                ambienteEnvio = empresaFiltro.getTipoambiente().getDescricao();
            }
        }

        List<MdfeTotalizadorFilter> totais = consultaTotalizadorService.findTotalizadores(ambienteEnvio);

        return !totais.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(totais) : ResponseEntity.noContent().build();
    }

}
