package br.com.conexao.mdfe.api.repository.permissao;

import br.com.conexao.mdfe.api.model.permissao.Permissao;
import br.com.conexao.mdfe.api.model.permissao.PermissaoGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissaoRepository extends JpaRepository<Permissao, Long>{

    public List<Permissao> findAllByFlagvisivelIsTrue();

    public List<Permissao> findAllByGrupoAndFlagvisivelIsTrue(PermissaoGrupo permissaoGrupo);


}
