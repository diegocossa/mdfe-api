package br.com.conexao.mdfe.api.service.ciot;

import br.com.conexao.mdfe.api.model.ciot.Ciot;
import br.com.conexao.mdfe.api.repository.ciot.CiotRepository;
import br.com.conexao.mdfe.api.service.empresa.EmpresaService;
import br.com.conexao.mdfe.api.service.motorista.MotoristaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class CiotService {

    @Autowired
    private CiotRepository ciotRepository;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private MotoristaService motoristaService;

    public List<Ciot> findAll(){
        return ciotRepository.findAllByFlagdelIsFalse();
    }

    public Ciot findByIdCiot(Long idciot){
        return ciotRepository.findByIdciotAndFlagdelIsFalse(idciot);
    }

    @Transactional
    public Ciot salvar(Ciot ciot){
        return ciotRepository.save(ciot);
    }

    @Transactional
    public void deletar(Long idciot){
        Ciot ciot = buscarCiotPeloId(idciot);
        ciot.setFlagdel(true);
        this.salvar(ciot);
    }

    @Transactional
    public Ciot atualizar(Long idciot, Ciot ciot){
        Ciot ciotSalvo = buscarCiotPeloId(idciot);
        BeanUtils.copyProperties(ciot, ciotSalvo, "idciot");
        return ciotSalvo;
    }

    private Ciot buscarCiotPeloId(Long idciot) {
        Ciot ciotSalvo = findByIdCiot(idciot);
        if (isNull(ciotSalvo)){
            throw new EmptyResultDataAccessException(1);
        }
        return ciotSalvo;
    }

    public Ciot buscarPelaEmpresa(Long idempresa) {
        Ciot ciotSalvo = ciotRepository.findByEmpresaAndFlagdelIsFalse(empresaService.findByIdEmpresa(idempresa));
        return ciotSalvo;
    }

    public Ciot buscarPeloMotorista(Long idmotorista) {
        Ciot ciotSalvo = ciotRepository.findByMotoristaAndFlagdelIsFalse(motoristaService.findByIdMotorista(idmotorista));
        return ciotSalvo;
    }

}
