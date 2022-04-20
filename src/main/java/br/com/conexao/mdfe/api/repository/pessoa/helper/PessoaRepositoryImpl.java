package br.com.conexao.mdfe.api.repository.pessoa.helper;

import br.com.conexao.mdfe.api.model.pessoa.Pessoa;
import br.com.conexao.mdfe.api.model.pessoa.PessoaPublic;
import br.com.conexao.mdfe.api.model.pessoa.TipoUsuario;
import br.com.conexao.mdfe.api.repository.util.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    public Page<PessoaPublic> filterOnPublic(Pessoa motoristaProjection, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<PessoaPublic> criteria = builder.createQuery(PessoaPublic.class);
        Root<PessoaPublic> root = criteria.from(PessoaPublic.class);

        Predicate[] predicates = createRestricoes(motoristaProjection, builder, root, true);
        criteria.where(predicates);

        TypedQuery<PessoaPublic> query = manager.createQuery(criteria);
        PageableUtil.addPageableIntoQuery(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(motoristaProjection, true));
    }

    public Page<Pessoa> filter(Pessoa motoristaProjection, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);

        Predicate[] predicates = createRestricoes(motoristaProjection, builder, root, false);
        criteria.where(predicates);

        TypedQuery<Pessoa> query = manager.createQuery(criteria);

        PageableUtil.addPageableIntoQuery(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(motoristaProjection, false));
    }

    private Long total(Pessoa pessoa, Boolean onPublic) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);

        Predicate[] predicates = createRestricoes(pessoa, builder, root, onPublic);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

    private Predicate[] createRestricoes(Pessoa pessoa, CriteriaBuilder builder, Root<?> root, Boolean onPublic) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(pessoa.getNome())) {
            predicates.add(
                    builder.like(root.get("nome"), "%" + pessoa.getNome() + "%")
            );
        }
        if (!StringUtils.isEmpty(pessoa.getEmail())) {
            predicates.add(
                    builder.like(root.get("email"), "%" + pessoa.getEmail() + "%")
            );
        }

        predicates.add(builder.isFalse(root.get("flagdel")));

        predicates.add(builder.notEqual(builder.trim(root.get("nome")), "3G Brasil"));

        //mostra todos os usuarios menos motorista
        Expression<TipoUsuario> tipoUsuario = root.get("tipousuario");
        Predicate motorista = builder.equal(tipoUsuario, TipoUsuario.MOTORISTA);

        predicates.add(builder.not(motorista));

        return predicates.toArray(new Predicate[predicates.size()]);
    }
}
