package dev.jgregorio.demo.data.infrastructure.persistence.user.search;

import dev.jgregorio.demo.data.domain.user.UserSearch;
import dev.jgregorio.demo.data.infrastructure.persistence.user.UserEntity;
import dev.jgregorio.demo.data.infrastructure.persistence.user.UserEntity_;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.*;

import java.util.*;

import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;

public final class UserSearchSpecification {

    private UserSearchSpecification() {
        throw new IllegalStateException("Class is not instantiable.");
    }

    public static Specification<UserEntity> search(final UserSearch criteria) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = createPredicates(root, query, cb, criteria);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static List<Predicate> createPredicates(
            Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb, UserSearch criteria) {
        final List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(criteria.fullName())) {
            predicates.add(fullname(criteria).toPredicate(root, query, cb));
        }
        return predicates;
    }

    private static Specification<UserEntity> fullname(UserSearch criteria) {
        return (root, query, cb) -> {
            Expression<String> name = cb.lower(root.get(UserEntity_.name));
            Expression<String> surname = cb.lower(root.get(UserEntity_.surname));
            String[] searchTerms =
                    criteria.fullName().trim().toLowerCase().replace(",", "").split("\\s+");
            List<Predicate> termPredicates =
                    Arrays.stream(searchTerms)
                            .filter(term -> !term.isEmpty())
                            .map(term -> buildNameOrSurnamePredicate(cb, name, surname, term))
                            .toList();
            return cb.and(termPredicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate buildNameOrSurnamePredicate(
            CriteriaBuilder cb, Expression<String> name, Expression<String> surname, String term) {
        HibernateCriteriaBuilder hcb = (HibernateCriteriaBuilder) cb;
        Expression<String> collatedName = hcb.collate(name, "BINARY_AI");
        Expression<String> collatedSurname = hcb.collate(surname, "BINARY_AI");
        final String pattern = "%" + term + "%";
        return cb.or(cb.like(collatedName, pattern), cb.like(collatedSurname, pattern));
    }
}
