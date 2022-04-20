package br.com.conexao.mdfe.api.resource.permissao;

import br.com.conexao.mdfe.api.model.permissao.Permissao;
import br.com.conexao.mdfe.api.model.permissao.PermissaoGrupo;
import br.com.conexao.mdfe.api.service.permissao.PermissaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permissoes")
public class PermissaoResource {

    @Autowired
    private PermissaoService permissaoService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_PERMISSAO') and #oauth2.hasScope('read')")
    public ResponseEntity listarPermissoes(){
        List<Permissao> permissoes = permissaoService.findAll();
        return !permissoes.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(permissoes) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{grupo}")
    @PreAuthorize("hasAuthority('ROLE_CONSULTAR_PERMISSAO') and #oauth2.hasScope('read')")
    public ResponseEntity listarPermissoesPorGrupo(@PathVariable String grupo){
        PermissaoGrupo permissaoGrupo = PermissaoGrupo.valueOf(grupo.toUpperCase());

        List<Permissao> permissoes = permissaoService.findAllByIdGrupo(permissaoGrupo);
        return !permissoes.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(permissoes) : ResponseEntity.notFound().build();
    }

}
