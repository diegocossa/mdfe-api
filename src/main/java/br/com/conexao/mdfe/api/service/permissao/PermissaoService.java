package br.com.conexao.mdfe.api.service.permissao;

import br.com.conexao.mdfe.api.model.permissao.Permissao;
import br.com.conexao.mdfe.api.model.permissao.PermissaoGrupo;
import br.com.conexao.mdfe.api.repository.permissao.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissaoService {

    @Autowired
    private PermissaoRepository repository;

    public List<Permissao> findAll(){
        return repository.findAllByFlagvisivelIsTrue();
    }

    public List<Permissao> findAllByIdGrupo(PermissaoGrupo permissaoGrupo){
        return repository.findAllByGrupoAndFlagvisivelIsTrue(permissaoGrupo);
    }
}
