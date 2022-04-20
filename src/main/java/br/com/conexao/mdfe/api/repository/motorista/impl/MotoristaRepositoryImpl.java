package br.com.conexao.mdfe.api.repository.motorista.impl;

import br.com.conexao.mdfe.api.model.motorista.Motorista;
import br.com.conexao.mdfe.api.model.motorista.MotoristaProjection;
import br.com.conexao.mdfe.api.model.motorista.Motorista_;
import br.com.conexao.mdfe.api.repository.util.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class MotoristaRepositoryImpl implements MotoristaRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Motorista> filter(MotoristaProjection motoristaProjection, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Motorista> criteria = builder.createQuery(Motorista.class);
        Root<Motorista> root = criteria.from(Motorista.class);

        Predicate[] predicates = createRestricoes(motoristaProjection, builder, root);
        criteria.where(predicates);

        TypedQuery<Motorista> query = manager.createQuery(criteria);

        PageableUtil.addPageableIntoQuery(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(motoristaProjection));
    }

    private Long total(MotoristaProjection motoristaProjection) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Motorista> root = criteria.from(Motorista.class);

        Predicate[] predicates = createRestricoes(motoristaProjection, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

    private Predicate[] createRestricoes(MotoristaProjection motoristaProjection, CriteriaBuilder builder, Root<Motorista> root) {
        List<Predicate> predicates = new ArrayList<>();

        if(!StringUtils.isEmpty(motoristaProjection.getNome())){
            predicates.add(
                    builder.like(builder.lower(root.get(Motorista_.nome)), "%" + motoristaProjection.getNome().toLowerCase() + "%")
            );
        }
        if(!StringUtils.isEmpty(motoristaProjection.getCpf())){
            predicates.add(
                    builder.like(root.get(Motorista_.cpf), "%" + motoristaProjection.getCpf() + "%")
            );
        }

        predicates.add(builder.isFalse(root.get(Motorista_.flagdel)));

        return predicates.toArray(new Predicate[predicates.size()]);
    }

}
