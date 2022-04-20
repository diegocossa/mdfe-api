package br.com.conexao.mdfe.api.repository.veiculo.impl;

import br.com.conexao.mdfe.api.model.veiculo.Veiculo;
import br.com.conexao.mdfe.api.model.veiculo.VeiculoProjection;
import br.com.conexao.mdfe.api.model.veiculo.Veiculo_;
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

public class VeiculoRepositoryImpl implements VeiculoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    public Page<Veiculo> filter(VeiculoProjection veiculoProjection, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Veiculo> criteria = builder.createQuery(Veiculo.class);
        Root<Veiculo> root = criteria.from(Veiculo.class);

        Predicate[] predicates = createRestricoes(veiculoProjection, builder, root);
        criteria.where(predicates);

        TypedQuery<Veiculo> query = manager.createQuery(criteria);

        PageableUtil.addPageableIntoQuery(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(veiculoProjection));
    }

    private Long total(VeiculoProjection veiculoProjection) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Veiculo> root = criteria.from(Veiculo.class);

        Predicate[] predicates = createRestricoes(veiculoProjection, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

    private Predicate[] createRestricoes(VeiculoProjection veiculoProjection, CriteriaBuilder builder, Root<Veiculo> root) {
        List<Predicate> predicates = new ArrayList<>();

        if(!StringUtils.isEmpty(veiculoProjection.getPlaca())){
            predicates.add(
                    builder.like(builder.lower(root.get(Veiculo_.placa)), "%" + veiculoProjection.getPlaca().toLowerCase() + "%")
            );
        }
        if(!StringUtils.isEmpty(veiculoProjection.getVeiculoTipo())){
            predicates.add(
                    builder.like(root.get(Veiculo_.veiculotipo.toString()), veiculoProjection.getVeiculoTipo().toString())
            );
        }
        predicates.add(builder.isFalse(root.get(Veiculo_.flagdel)));

        return predicates.toArray(new Predicate[predicates.size()]);
    }
}
