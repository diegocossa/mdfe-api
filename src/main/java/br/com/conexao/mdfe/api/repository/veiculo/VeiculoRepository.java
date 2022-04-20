package br.com.conexao.mdfe.api.repository.veiculo;

import br.com.conexao.mdfe.api.model.veiculo.Veiculo;
import br.com.conexao.mdfe.api.model.veiculo.VeiculoTipo;
import br.com.conexao.mdfe.api.repository.veiculo.impl.VeiculoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long>, VeiculoRepositoryQuery {

    public List<Veiculo> findAllByFlagdelIsFalse();

    public Veiculo findByIdveiculoAndFlagdelIsFalse(Long idveiculo);

    public List<Veiculo> findAllByVeiculotipoAndFlagdelIsFalse(VeiculoTipo veiculoTipo);

}
