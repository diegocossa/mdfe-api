package br.com.conexao.mdfe.api.service.mdfe;

import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.model.mdfe.SituacaoMDFe;
import br.com.conexao.mdfe.api.repository.mdfe.MDFeRepository;
import br.com.conexao.mdfe.api.service.empresa.SequenceNumMdfService;
import br.com.conexao.mdfe.api.tenant.TenantIdEmpresaContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class MdfeLancamentoService {

    @Autowired
    private MDFeRepository MDFeRepository;

    @Autowired
    private SequenceNumMdfService sequenceNumMdfService;

    public List<MDFe> findAll() {
        return MDFeRepository.findAll();
    }

    public MDFe findByIdMdfe(Long idmdfe) {
        return MDFeRepository.findByIdmdfe(idmdfe);
    }

    public MDFe findByChave(String chave) {
        return MDFeRepository.findByChave(chave);
    }

    @Transactional
    public MDFe atualizar(Long idmdfe, MDFe mdfe) {

        MDFeRepository.delete(idmdfe);

        return MDFeRepository.save(mdfe);
    }

    @Transactional
    public void atualizaStatusMdfe(MDFe mdfe) {
        MDFeRepository.save(mdfe);
    }

    @Transactional
    public MDFe salvar(MDFe mdfe) {

        //Busca a sequence no ultimo momento da gravação
        if (isNull(mdfe.getNmdf())) {
            mdfe.setNmdf(String.valueOf(sequenceNumMdfService.getProximoValorSequence(TenantIdEmpresaContext.getCurrentTenant())));
        }

        return MDFeRepository.saveAndFlush(mdfe);
    }

    @Transactional
    public void deletar(Long idmdfe) throws Exception {

        MDFe MDFe = buscarMdfePeloId(idmdfe);

        if (MDFe.getSituacao().equals(SituacaoMDFe.GRAVADO)) {
            MDFeRepository.delete(idmdfe);
        }else{
            throw new Exception("Esse MDFe não pode ser excluído.");
        }
    }

    public MDFe buscarMdfePeloId(Long idmdfe) {
        MDFe mdfeSalva = findByIdMdfe(idmdfe);
        if (isNull(mdfeSalva)) {
            throw new EmptyResultDataAccessException(1);
        }
        return mdfeSalva;
    }
}
