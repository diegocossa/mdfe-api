package br.com.conexao.mdfe.api.repository.seguradora.impl;

import br.com.conexao.mdfe.api.model.seguradora.Seguradora;
import br.com.conexao.mdfe.api.model.seguradora.Seguradora_;
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

public class SeguradoraRepositoryImpl implements SeguradoraRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    public Page<Seguradora> filter(Seguradora motoristaProjection, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Seguradora> criteria = builder.createQuery(Seguradora.class);
        Root<Seguradora> root = criteria.from(Seguradora.class);

        Predicate[] predicates = createRestricoes(motoristaProjection, builder, root);
        criteria.where(predicates);

        TypedQuery<Seguradora> query = manager.createQuery(criteria);

        PageableUtil.addPageableIntoQuery(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(motoristaProjection));
    }

    private Long total(Seguradora seguradora) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Seguradora> root = criteria.from(Seguradora.class);

        Predicate[] predicates = createRestricoes(seguradora, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

    private Predicate[] createRestricoes(Seguradora seguradora, CriteriaBuilder builder, Root<Seguradora> root) {
        List<Predicate> predicates = new ArrayList<>();

        if(!StringUtils.isEmpty(seguradora.getCnpjseguradora())){
            predicates.add(
                    builder.like(root.get(Seguradora_.cnpjseguradora), "%" + seguradora.getCnpjseguradora() + "%")
            );
        }
        if(!StringUtils.isEmpty(seguradora.getNumeroapolice())){
            predicates.add(
                    builder.like(root.get(Seguradora_.numeroapolice), "%" + seguradora.getNumeroapolice() + "%")
            );
        }

        predicates.add(builder.isFalse(root.get(Seguradora_.flagdel)));

        return predicates.toArray(new Predicate[predicates.size()]);
    }
}
