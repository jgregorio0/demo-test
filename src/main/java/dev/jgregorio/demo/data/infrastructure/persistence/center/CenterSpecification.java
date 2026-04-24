package dev.jgregorio.demo.data.infrastructure.persistence.center;

import dev.jgregorio.demo.data.domain.center.CenterSearch;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;

public class CenterSpecification {

    private CenterSpecification() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<CenterEntity> search(final CenterSearch criteria) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (criteria.name() != null && !criteria.name().isBlank()) {
                predicates.add(name(criteria.name()).toPredicate(root, query, cb));
            }
            if (criteria.address() != null && !criteria.address().isBlank()) {
                predicates.add(address(criteria.address()).toPredicate(root, query, cb));
            }
            if (criteria.postalCode() != null && !criteria.postalCode().isBlank()) {
                predicates.add(postalCode(criteria.postalCode()).toPredicate(root, query, cb));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<CenterEntity> name(String name) {
        return (root, query, cb) -> collate(cb, root.get(CenterEntity_.name), name);
    }

    public static Specification<CenterEntity> postalCode(String postalCode) {
        return (root, query, cb) -> collate(cb, root.get(CenterEntity_.postalCode), postalCode);
    }

    public static Specification<CenterEntity> address(String address) {
        return (root, query, cb) -> collate(cb, root.get(CenterEntity_.address), address);
    }

    private static Predicate collate(CriteriaBuilder cb, Path<String> path, String value) {
        if (cb instanceof HibernateCriteriaBuilder hcb && supportsCollation(hcb)) {
            return cb.like(hcb.collate(cb.lower(path), "BINARY_AI"), like(value));
        }
        return cb.like(cb.lower(path), like(value));
    }

    private static boolean supportsCollation(HibernateCriteriaBuilder hcb) {
        try {
            // HibernateCriteriaBuilder extends NodeBuilder which has access to the SessionFactory
            SessionFactoryImplementor sf = (SessionFactoryImplementor)
                    ((org.hibernate.query.sqm.NodeBuilder) hcb).getSessionFactory();
            return !(sf.getJdbcServices().getDialect() instanceof H2Dialect);
        } catch (Exception e) {
            return false;
        }
    }

    private static String like(String value) {
        return "%" + value.trim().toLowerCase() + "%";
    }
}
