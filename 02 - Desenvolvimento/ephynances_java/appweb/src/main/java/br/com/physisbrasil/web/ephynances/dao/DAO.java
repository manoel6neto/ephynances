/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.physisbrasil.web.ephynances.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
//import org.eclipse.persistence.jpa.JpaQuery;

/**
 *
 * @author Thadeu
 */
public abstract class DAO<T> {

    public static final String PERSISTENCE_UNIT_NAME = "ephynancesPU";

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    protected EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    protected Class<T> entityClass;

    @PostConstruct
    public void init() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        this.entityClass = (Class<T>) actualTypeArguments[0];
    }

    public void flush() {
        getEntityManager().flush();
    }

    public void clearCache() {
        getEntityManager().getEntityManagerFactory().getCache().evictAll();
    }

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void detach(T entity) {
        getEntityManager().detach(entity);
    }

    public void refresh(T entity) {
        getEntityManager().refresh(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findAll(String defaultOrder) {
        return findAll(defaultOrder, true);
    }

    public List<T> findAll(String defaultOrder, boolean asc) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        Root<T> from = cq.from(entityClass);
        cq.select(from);
        if (asc) {
            cq.orderBy(cb.asc(from.get(defaultOrder)));
        } else {
            cq.orderBy(cb.desc(from.get(defaultOrder)));
        }
        return getEntityManager().createQuery(cq).getResultList();
    }

    public T findByProperty(String property, Object value) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        Root<T> from = cq.from(entityClass);
        cq.select(from);
        cq.where(cb.equal(from.get(property), value));

        T singleResult;
        try {
            singleResult = getEntityManager().createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            singleResult = null;
        }
        return singleResult;
    }

    public List<T> listByProperty(String property, Object value) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        Root<T> from = cq.from(entityClass);
        cq.select(from);

        String[] cols = property.split("\\.");
        Path<Object> path = from.get(cols[0]);
        for (int i = 1; i < cols.length; i++) {
            path = path.get(cols[i]);
        }

        cq.where(cb.equal(path, value));

        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> listByProperties(Map<String, String> properties) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        Root<T> from = cq.from(entityClass);
        cq.select(from);

        Predicate[] restrictions = generateRestrictions(from, cb, properties);

        cq.where(cb.and(restrictions));

        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int first, int pageSize) {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(pageSize);
        q.setFirstResult(first);

        return q.getResultList();
    }

    public void getMetamodel(Root<T> root) {
    }

    public List<T> findRange(int first, int pageSize, String sortField, boolean asc, Map<String, String> filters) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);

        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        Predicate[] restrictions = generateRestrictions(root, criteriaBuilder, filters);
        Predicate predicates = criteriaBuilder.and(restrictions);
        criteriaQuery.where(criteriaBuilder.and(predicates));

        if (sortField != null) {
            Path<?> path = root;
            String[] sortCols = sortField.split("\\.");
            for (String sc : sortCols) {
                path = path.get(sc);
            }
            if (asc) {
                criteriaQuery.orderBy(criteriaBuilder.asc(path));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(path));
            }
        }

        Query q = getEntityManager().createQuery(criteriaQuery);
        q.setMaxResults(pageSize);
        q.setFirstResult(first);

        return q.getResultList();
    }

    public List<T> findRange(int first, int pageSize, String sortField, boolean asc, Map<String, String> andFilters, Map<String, String> orFilters) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);

        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        Predicate[] restrictionsForAnd = generateRestrictions(root, criteriaBuilder, andFilters);
        Predicate[] restrictionsForOr = generateRestrictions(root, criteriaBuilder, orFilters);

        Predicate predicatesWithAnd = criteriaBuilder.and(restrictionsForAnd);
        Predicate predicatesWithOr = criteriaBuilder.or(restrictionsForOr);

        criteriaQuery.where(criteriaBuilder.and(predicatesWithAnd, predicatesWithOr));

        if (sortField != null) {
            Path<?> path = root;
            String[] sortCols = sortField.split("\\.");
            for (String sc : sortCols) {
                path = path.get(sc);
            }
            if (asc) {
                criteriaQuery.orderBy(criteriaBuilder.asc(path));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(path));
            }
        }

        Query q = getEntityManager().createQuery(criteriaQuery);
        q.setMaxResults(pageSize);
        q.setFirstResult(first);

        List list = q.getResultList();
//        System.out.println(q.unwrap(JpaQuery.class).getDatabaseQuery().getSQLString());
        return list;
    }

    public T findSingleByQuery(String queryName, String... params) {
        TypedQuery<T> namedQuery = getEntityManager().createNamedQuery(queryName, entityClass);

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                namedQuery.setParameter(i + 1, params[i]);
            }
        }
        return namedQuery.getSingleResult();
    }

    public List<T> findByQuery(String queryName, String... params) {
        TypedQuery<T> namedQuery = getEntityManager().createNamedQuery(queryName, entityClass);

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                namedQuery.setParameter(i + 1, params[i]);
            }
        }
        return namedQuery.getResultList();
    }

    public int count(Map<String, String> filters) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);

        Root<T> root = criteria.from(entityClass);
        criteria.select(criteriaBuilder.count(root));

        Predicate[] restrictions = generateRestrictions(root, criteriaBuilder, filters);
        Predicate predicates = criteriaBuilder.and(restrictions);
        criteria.where(criteriaBuilder.and(predicates));

        Query q = getEntityManager().createQuery(criteria);

        return ((Long) q.getSingleResult()).intValue();
    }

    public int count(Map<String, String> andFilters, Map<String, String> orFilters) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(criteriaBuilder.count(root));

        Predicate[] restrictionsForAnd = generateRestrictions(root, criteriaBuilder, andFilters);
        Predicate[] restrictionsForOr = generateRestrictions(root, criteriaBuilder, orFilters);

        Predicate predicatesWithAnd = criteriaBuilder.and(restrictionsForAnd);
        Predicate predicatesWithOr = criteriaBuilder.or(restrictionsForOr);

        criteriaQuery.where(criteriaBuilder.and(predicatesWithAnd, predicatesWithOr));

        Query q = getEntityManager().createQuery(criteriaQuery);

        return ((Long) q.getSingleResult()).intValue();
    }

    public Predicate[] generateRestrictions(Root<T> root, CriteriaBuilder criteriaBuilder, Map<String, String> filters) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        if (filters != null) {
            for (String key : filters.keySet()) {
                Path<?> path = root;

                String[] matches = key.split("\\.");
                for (String match : matches) {
                    path = path.get(match);
                }

                Object value = filters.get(key);

                if (value == null) {
                    predicates.add(criteriaBuilder.isNull(path));
                } else if (value.toString().contains("%")) {
                    predicates.add(criteriaBuilder.like(path.as(String.class), value.toString()));
                } else {
                    predicates.add(criteriaBuilder.equal(path, value));
                }
            }
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }
}
