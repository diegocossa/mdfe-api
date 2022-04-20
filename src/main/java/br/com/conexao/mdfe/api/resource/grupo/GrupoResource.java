package br.com.conexao.mdfe.api.resource.grupo;

import br.com.conexao.mdfe.api.event.RecursoCriadoEvent;
import br.com.conexao.mdfe.api.model.grupo.Grupo;
import br.com.conexao.mdfe.api.service.grupo.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/grupos")
public class GrupoResource {

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_GRUPO') and #oauth2.hasScope('read')")
    public ResponseEntity listarGrupos(){
        List<Grupo> grupos = grupoService.findAll();
        return !grupos.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(grupos) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{idgrupo}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_GRUPO') and #oauth2.hasScope('read')")
    public ResponseEntity buscarPeloId(@PathVariable Long idgrupo){
        Grupo grupo = grupoService.findByIdGrupo(idgrupo);
        return grupo != null ? ResponseEntity.ok(grupo) : ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_GRUPO') and #oauth2.hasScope('write')")
    public ResponseEntity<Grupo> cadastrar(@Valid @RequestBody Grupo grupo, HttpServletResponse response) {
        Grupo grupoSalvo = grupoService.salvar(grupo);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, grupoSalvo.getIdgrupo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoSalvo);
    }

    @PutMapping("/{idgrupo}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_GRUPO') and #oauth2.hasScope('write')")
    public ResponseEntity<Grupo> atualizar(@PathVariable Long idgrupo, @Valid @RequestBody Grupo grupo){
        Grupo grupoSalvo = grupoService.atualizar(idgrupo,grupo);
        return ResponseEntity.status(HttpStatus.OK).body(grupoSalvo);
    }

    @DeleteMapping("/{idgrupo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_GRUPO') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long idgrupo){
        grupoService.deletar(idgrupo);
    }

}
