package br.com.conexao.mdfe.api.service.grupo;

import br.com.conexao.mdfe.api.model.grupo.Grupo;
import br.com.conexao.mdfe.api.repository.grupo.GrupoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class GrupoService {

    @Autowired
    private GrupoRepository grupoRepository;

    public List<Grupo> findAll(){
        return grupoRepository.findAllByFlagdelIsFalse();
    }

    public Grupo findByIdGrupo(Long idgrupo){
        return grupoRepository.findByIdgrupoAndFlagdelIsFalse(idgrupo);
    }

    @Transactional
    public Grupo salvar(Grupo grupo){
        return grupoRepository.save(grupo);
    }

    @Transactional
    public void deletar(Long idgrupo){
        Grupo grupo = buscarGrupoPeloId(idgrupo);
        grupo.setFlagdel(true);
        this.salvar(grupo);
    }

    @Transactional
    public Grupo atualizar(Long idgrupo, Grupo grupo){
        Grupo grupoSalvo = buscarGrupoPeloId(idgrupo);
        BeanUtils.copyProperties(grupo, grupoSalvo, "idgrupo");
        return grupoSalvo;
    }

    private Grupo buscarGrupoPeloId(Long idgrupo) {
        Grupo grupoSalvo = findByIdGrupo(idgrupo);
        if (isNull(grupoSalvo)){
            throw new EmptyResultDataAccessException(1);
        }
        return grupoSalvo;
    }

}
