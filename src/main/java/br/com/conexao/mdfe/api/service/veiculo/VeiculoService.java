package br.com.conexao.mdfe.api.service.veiculo;

import br.com.conexao.mdfe.api.model.proprietario.Proprietario;
import br.com.conexao.mdfe.api.model.veiculo.Veiculo;
import br.com.conexao.mdfe.api.model.veiculo.VeiculoTipo;
import br.com.conexao.mdfe.api.repository.proprietario.ProprietarioRepository;
import br.com.conexao.mdfe.api.repository.veiculo.VeiculoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ProprietarioRepository proprietarioRepository;

    public List<Veiculo> findAll(){
        return veiculoRepository.findAllByFlagdelIsFalse();
    }

    public Veiculo findByIdVeiculo(Long idveiculo){
        return veiculoRepository.findByIdveiculoAndFlagdelIsFalse(idveiculo);
    }

    public List<Veiculo> findAllByVeiculoTipo(VeiculoTipo veiculoTipo){
        return veiculoRepository.findAllByVeiculotipoAndFlagdelIsFalse(veiculoTipo);
    }

    @Transactional
    public Veiculo salvar(Veiculo veiculo){
        if (veiculo.getProprietario() != null) {
            proprietarioRepository.save(veiculo.getProprietario());
        }
        return veiculoRepository.save(veiculo);
    }

    @Transactional
    public void deletar(Long idveiculo){
        Veiculo veiculo = buscarVeiculoPeloId(idveiculo);
        veiculo.setFlagdel(true);
        this.salvar(veiculo);
    }

    @Transactional
    public Veiculo atualizar(Long idveiculo, Veiculo veiculo){
        Veiculo veiculoSalvo = buscarVeiculoPeloId(idveiculo);
        BeanUtils.copyProperties(veiculo, veiculoSalvo, "idveiculo, proprietario");
        Proprietario proprietarioSalvo = proprietarioRepository.findOne(veiculo.getProprietario().getIdproprietario());
        BeanUtils.copyProperties(veiculo.getProprietario(), proprietarioSalvo, "idproprietario");
        return veiculoSalvo;
    }

    private Veiculo buscarVeiculoPeloId(Long idveiculo) {
        Veiculo veiculoSalvo = findByIdVeiculo(idveiculo);
        if (isNull(veiculoSalvo)){
            throw new EmptyResultDataAccessException(1);
        }
        return veiculoSalvo;
    }
}
