package br.com.conexao.mdfe.api.repository.ibge;

import br.com.conexao.mdfe.api.model.ibge.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    @Query("select e from Estado e order by e.nome asc")
    List<Estado> findAll();
}
