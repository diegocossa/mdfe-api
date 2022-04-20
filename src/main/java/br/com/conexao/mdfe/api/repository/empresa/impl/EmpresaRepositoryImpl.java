package br.com.conexao.mdfe.api.repository.empresa.impl;

import br.com.conexao.mdfe.api.model.empresa.Empresa;
import br.com.conexao.mdfe.api.model.empresa.EmpresaProjection;
import br.com.conexao.mdfe.api.model.empresa.Empresa_;
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

public class EmpresaRepositoryImpl implements EmpresaRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Empresa> filter(EmpresaProjection empresaProjection, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Empresa> criteria = builder.createQuery(Empresa.class);
        Root<Empresa> root = criteria.from(Empresa.class);

        Predicate[] predicates = createRestricoes(empresaProjection, builder, root);
        criteria.where(predicates);

        TypedQuery<Empresa> query = manager.createQuery(criteria);

        PageableUtil.addPageableIntoQuery(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(empresaProjection));
    }

    private Long total(EmpresaProjection empresaProjection) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Empresa> root = criteria.from(Empresa.class);

        Predicate[] predicates = createRestricoes(empresaProjection, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

    private Predicate[] createRestricoes(EmpresaProjection empresaProjection, CriteriaBuilder builder, Root<Empresa> root) {
        List<Predicate> predicates = new ArrayList<>();

        if(!StringUtils.isEmpty(empresaProjection.getCnpj())) {
            predicates.add(
                    builder.like(root.get(Empresa_.cnpj), "%" + empresaProjection.getCnpj() + "%")
            );
        }
        if(!StringUtils.isEmpty(empresaProjection.getFantasia())) {
            predicates.add(
                    builder.like(root.get(Empresa_.fantasia), "%" + empresaProjection.getFantasia() + "%")
            );
        }
        if(!StringUtils.isEmpty(empresaProjection.getRazaosocial())) {
            predicates.add(
                    builder.like(root.get(Empresa_.razaosocial), "%" + empresaProjection.getRazaosocial() + "%")
            );
        }
        predicates.add(builder.isFalse(root.get(Empresa_.flagdel)));

        return predicates.toArray(new Predicate[predicates.size()]);
    }


}
