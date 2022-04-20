package br.com.conexao.mdfe.api.service.ibge;

import br.com.conexao.mdfe.api.model.ibge.Municipio;
import br.com.conexao.mdfe.api.repository.ibge.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    @Transactional
    public Municipio salvar(Municipio municipio) {
        return municipioRepository.save(municipio);
    }

    public List<Municipio> findByEstado_Id(Long id) {
        return municipioRepository.findByEstado_Id(id);
    }

    public Municipio findById(Long id) {
        return municipioRepository.findByIdOrderByNomeAsc(id);
    }

    @Transactional
    public void deleteAll() {
        municipioRepository.delete(findAll());
    }

    public List<Municipio> findAll() {
        return municipioRepository.findAll();
    }
}
