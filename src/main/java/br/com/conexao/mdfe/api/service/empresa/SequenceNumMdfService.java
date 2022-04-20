package br.com.conexao.mdfe.api.service.empresa;

import br.com.conexao.mdfe.api.model.empresa.Empresa;
import br.com.conexao.mdfe.api.model.empresa.SequenceNumMdf;
import br.com.conexao.mdfe.api.repository.empresa.SequenceNumMdfRepository;
import br.com.conexao.mdfe.api.tenant.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;

@Service
public class SequenceNumMdfService {

    private static final Logger logger = LoggerFactory.getLogger(SequenceNumMdfService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SequenceNumMdfRepository sequenceNumMdfRepository;

    public SequenceNumMdf salvar(SequenceNumMdf sequenceNumMdf){
        return sequenceNumMdfRepository.save(sequenceNumMdf);
    }

    public SequenceNumMdf criarSequenceNumMdf(Empresa empresa) {
        SequenceNumMdf sequence = new SequenceNumMdf();

        sequence.setIdempresa(empresa.getIdempresa());

        String nomeSequence = "seq_nummdf_".concat(String.valueOf(empresa.getIdempresa()));
        sequence.setIdsequence(nomeSequence);

        StringBuilder comandoCriacao = new StringBuilder();
        comandoCriacao.append("CREATE SEQUENCE ".concat(TenantContext.getCurrentTenant()).concat(".").concat(nomeSequence));
        comandoCriacao.append(" INCREMENT 1");
        comandoCriacao.append(" MINVALUE 1");
        comandoCriacao.append(" MAXVALUE 9223372036854775807");
        comandoCriacao.append(" START 1");
        comandoCriacao.append(" CACHE 1;");

        Query query = entityManager.createNativeQuery(comandoCriacao.toString());
        query.executeUpdate();


        return salvar(sequence);
    }

    public BigInteger getProximoValorSequence(Long idempresa){

        SequenceNumMdf sequence = sequenceNumMdfRepository.findByIdempresa(idempresa);

        String sql = "SELECT nextval('"+ TenantContext.getCurrentTenant().concat(".").concat(sequence.getIdsequence())+"\')";
        Query query = entityManager.createNativeQuery(sql);

        return (BigInteger) query.getSingleResult();
    }

    public BigInteger getValorAtualSequence(Long idempresa){

        SequenceNumMdf sequence = sequenceNumMdfRepository.findByIdempresa(idempresa);

        String sql = "SELECT last_value from " + TenantContext.getCurrentTenant().concat(".").concat(sequence.getIdsequence());
        Query query = entityManager.createNativeQuery(sql);

        return (BigInteger) query.getSingleResult();
    }

    @Transactional
    public void atualizarSequence(Long idempresa, Long valor){

        try {

            SequenceNumMdf sequence = sequenceNumMdfRepository.findByIdempresa(idempresa);

            String nomeSequence = TenantContext.getCurrentTenant().concat(".").concat(sequence.getIdsequence());

            String comandoInicio = "ALTER SEQUENCE ";

            String comandoFim = " RESTART WITH " + valor + " ;";

            Query query = entityManager.createNativeQuery( comandoInicio + nomeSequence + comandoFim);
            query.executeUpdate();

        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
    }
}
