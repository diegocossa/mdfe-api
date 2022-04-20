package br.com.conexao.mdfe.api.repository.ibge;

import br.com.conexao.mdfe.api.model.ibge.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

    List<Municipio> findByEstado_Id(Long id);

    Municipio findByIdOrderByNomeAsc(Long id);
}
