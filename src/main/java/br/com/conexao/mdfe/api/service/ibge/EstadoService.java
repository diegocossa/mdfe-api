package br.com.conexao.mdfe.api.service.ibge;

import br.com.conexao.mdfe.api.model.ibge.Estado;
import br.com.conexao.mdfe.api.repository.ibge.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    @Transactional
    public List<Estado> salvar(List<Estado> estado) {
        return estadoRepository.save(estado);
    }

    @Transactional
    public void deleteAll() {
        estadoRepository.deleteAll();
    }

    public List<Estado> findAll() {
        return estadoRepository.findAll();
    }
}
