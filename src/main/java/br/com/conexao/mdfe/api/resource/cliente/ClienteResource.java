package br.com.conexao.mdfe.api.resource.cliente;

import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.pessoa.Usuario;
import br.com.conexao.mdfe.api.service.cliente.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ValidaErro validaErro;

    @PostMapping("/novo")
    @PreAuthorize("hasAuthority('ROLE_PAINEL_ADM') and #oauth2.hasScope('write')")
    public ResponseEntity<Usuario> gerarNovoCliente(@Valid @RequestBody Usuario usuario, HttpServletRequest request){

        try {
            clienteService.criarNovoSchema(usuario, request);
        } catch (Exception ex) {
            validaErro.addErro("erro-migracao-novo-cliente", "Erro ao migrar novo cliente. " + ex.getMessage());
            validaErro.trataErros();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

}
