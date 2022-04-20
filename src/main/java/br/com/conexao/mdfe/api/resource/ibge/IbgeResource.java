package br.com.conexao.mdfe.api.resource.ibge;

import br.com.conexao.mdfe.api.service.ibge.EstadoService;
import br.com.conexao.mdfe.api.service.ibge.IbgeService;
import br.com.conexao.mdfe.api.service.ibge.MunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ibge")
public class IbgeResource {

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private MunicipioService municipioService;

    @Autowired
    private IbgeService ibgeService;

    @GetMapping("/estados")
    public ResponseEntity getEstados() {
        return ResponseEntity.ok(estadoService.findAll());
    }

    @GetMapping("/municipios/estado/{idUF}")
    public ResponseEntity getMunicipios(@PathVariable Long idUF) {
        return ResponseEntity.ok(municipioService.findByEstado_Id(idUF));
    }

    @GetMapping("/municipios/{idMunicipio}")
    public ResponseEntity getMunicipio(@PathVariable Long idMunicipio) {
        return ResponseEntity.ok(municipioService.findById(idMunicipio));
    }

    @PostMapping
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_SINCRONIZAR_IBGE') and #oauth2.hasScope('write')")
    public ResponseEntity sincronizar() {

        ibgeService.sincronizar();

        return ResponseEntity.status(HttpStatus.CREATED).body("Sincronização concluída.");
    }

}
