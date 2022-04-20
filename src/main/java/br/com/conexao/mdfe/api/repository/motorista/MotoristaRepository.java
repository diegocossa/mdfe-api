package br.com.conexao.mdfe.api.repository.motorista;

import br.com.conexao.mdfe.api.model.motorista.Motorista;
import br.com.conexao.mdfe.api.repository.motorista.impl.MotoristaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotoristaRepository extends JpaRepository<Motorista, Long>, MotoristaRepositoryQuery {

    public List<Motorista> findAllByFlagdelIsFalse();

    public Motorista findByIdmotoristaAndFlagdelIsFalse(Long idmotorista);

    public List<Motorista> findAllByCpf(String cpf);

    public Motorista findByCpf(String cpf);

    public Motorista findByCpfAndFlagdelIsFalse(String cpf);
}

