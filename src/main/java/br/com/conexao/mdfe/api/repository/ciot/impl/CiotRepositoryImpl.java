package br.com.conexao.mdfe.api.repository.ciot.impl;

import br.com.conexao.mdfe.api.model.ciot.Ciot;
import br.com.conexao.mdfe.api.model.ciot.Ciot_;
import br.com.conexao.mdfe.api.repository.util.PageableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CiotRepositoryImpl implements CiotRepositoryQuery {

    @Autowired
    private EntityManager manager;

    public Page<Ciot> filter(Ciot ciot, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Ciot> criteria = builder.createQuery(Ciot.class);
        Root<Ciot> root = criteria.from(Ciot.class);

        Predicate[] predicates = createRestricoes(ciot, builder, root);
        criteria.where(predicates);

        TypedQuery<Ciot> query = manager.createQuery(criteria);

        PageableUtil.addPageableIntoQuery(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(ciot));
    }

    private Long total(Ciot ciot) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Ciot> root = criteria.from(Ciot.class);

        Predicate[] predicates = createRestricoes(ciot, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

    private Predicate[] createRestricoes(Ciot ciot, CriteriaBuilder builder, Root<Ciot> root) {
        List<Predicate> predicates = new ArrayList<>();

        if(!StringUtils.isEmpty(ciot.getCiot())) {
            predicates.add(
                    builder.like(root.get(Ciot_.ciot), "%" + ciot.getCiot() + "%")
            );
        }
        predicates.add(builder.isFalse(root.get(Ciot_.flagdel)));

        return predicates.toArray(new Predicate[predicates.size()]);
    }
}
