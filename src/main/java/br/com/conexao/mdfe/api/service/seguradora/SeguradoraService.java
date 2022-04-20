package br.com.conexao.mdfe.api.service.seguradora;

import br.com.conexao.mdfe.api.model.seguradora.Seguradora;
import br.com.conexao.mdfe.api.repository.seguradora.SeguradoraRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class SeguradoraService {

    @Autowired
    private SeguradoraRepository seguradoraRepository;

    public List<Seguradora> findAll(){
        return seguradoraRepository.findAllByFlagdelIsFalse();
    }

    public Seguradora findByIdSeguradora(Long idseguradora){
        return seguradoraRepository.findByidSeguradoraAndFlagdelIsFalse(idseguradora);
    }

    @Transactional
    public Seguradora salvar(Seguradora seguradora){
        return seguradoraRepository.save(seguradora);
    }

    @Transactional
    public void deletar(Long idseguradora){
        Seguradora seguradora = buscarSeguradoraPeloId(idseguradora);
        seguradora.setFlagdel(true);
        this.salvar(seguradora);
    }

    @Transactional
    public Seguradora atualizar(Long idseguradora, Seguradora seguradora){
        Seguradora seguradoraSalva = buscarSeguradoraPeloId(idseguradora);
        BeanUtils.copyProperties(seguradora, seguradoraSalva, "idseguradora");
        return seguradoraSalva;
    }

    private Seguradora buscarSeguradoraPeloId(Long idseguradora) {
        Seguradora seguradoraSalva = findByIdSeguradora(idseguradora);
        if (isNull(seguradoraSalva)){
            throw new EmptyResultDataAccessException(1);
        }
        return seguradoraSalva;
    }

}
