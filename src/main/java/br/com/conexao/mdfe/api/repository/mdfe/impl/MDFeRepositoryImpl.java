package br.com.conexao.mdfe.api.repository.mdfe.impl;

import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.model.mdfe.MDFe_;
import br.com.conexao.mdfe.api.model.veiculo.Veiculo_;
import br.com.conexao.mdfe.api.repository.mdfe.filter.MdfeFilter;
import br.com.conexao.mdfe.api.repository.mdfe.projection.MDFeProjection;
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

public class MDFeRepositoryImpl implements MDFeRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<MDFe> filter(MdfeFilter mdfeFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<MDFe> criteria = builder.createQuery(MDFe.class);
        Root<MDFe> root = criteria.from(MDFe.class);

        Predicate[] predicates = createRestricoes(mdfeFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<MDFe> query = manager.createQuery(criteria);

        PageableUtil.addPageableIntoQuery(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(mdfeFilter));
    }

    @Override
    public Page<MDFeProjection> resume(MdfeFilter mdfeFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<MDFeProjection> criteria = builder.createQuery(MDFeProjection.class);
        Root<MDFe> root = criteria.from(MDFe.class);

        criteria.select(builder.construct(MDFeProjection.class,
                root.get(MDFe_.idmdfe),
                root.get(MDFe_.retornorecibo),
                root.get(MDFe_.nmdf),
                root.get(MDFe_.serie),
                root.get(MDFe_.veiculotracao),
                root.get(MDFe_.situacao),
                root.get(MDFe_.chave)));

        Predicate[] predicates = createRestricoes(mdfeFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<MDFeProjection> query = manager.createQuery(criteria);

        PageableUtil.addPageableIntoQuery(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(mdfeFilter));
    }

    private Long total(MdfeFilter mdfeFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<MDFe> root = criteria.from(MDFe.class);

        Predicate[] predicates = createRestricoes(mdfeFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

    private Predicate[] createRestricoes(MdfeFilter filter, CriteriaBuilder builder, Root<MDFe> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getSituacao() != null) {
            predicates.add(builder.equal(root.get(MDFe_.situacao), filter.getSituacao()));
        }

        if (!StringUtils.isEmpty(filter.getNmdf())) {
            predicates.add(builder.like(builder.lower(root.get(MDFe_.nmdf)), "%" + filter.getNmdf().toLowerCase() + "%"));
        }

        if (!StringUtils.isEmpty(filter.getChave())) {
            predicates.add(builder.like(builder.lower(root.get(MDFe_.chave)), "%" + filter.getChave().toLowerCase() + "%"));
        }

        if (!StringUtils.isEmpty(filter.getNumprotocolo())) {
            predicates.add(builder.like(builder.lower(root.get(MDFe_.numprotocolo)), "%" + filter.getNumprotocolo().toLowerCase() + "%"));
        }

        if (filter.getSerie() != null) {
            predicates.add(builder.equal(root.get(MDFe_.serie), filter.getSerie()));
        }

        if (filter.getUfini() != null) {
            predicates.add(builder.equal(builder.lower(root.get(MDFe_.ufini)), filter.getUfini().getCodigo().toLowerCase()));
        }

        if (filter.getUffim() != null) {
            predicates.add(builder.equal(builder.lower(root.get(MDFe_.uffim)), filter.getUffim().getCodigo().toLowerCase()));
        }

        if (filter.getAmbienteenvio() != null) {
            predicates.add(builder.or(
                    builder.equal(builder.lower(root.get(MDFe_.ambienteenvio)), filter.getAmbienteenvio().toLowerCase()),
                    builder.isNull(root.get(MDFe_.ambienteenvio))
                    )
            );
        }

        if (!StringUtils.isEmpty(filter.getPlaca())) {
            predicates.add(builder.like(builder.lower(root.get(MDFe_.veiculotracao).get(Veiculo_.placa)),
                    "%" + filter.getPlaca().toLowerCase() + "%"));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

}
