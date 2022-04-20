package br.com.conexao.mdfe.api.repository.grupo;

import br.com.conexao.mdfe.api.model.grupo.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    public List<Grupo> findAllByFlagdelIsFalse();

    public Grupo findByIdgrupoAndFlagdelIsFalse(Long idgrupo);

}
