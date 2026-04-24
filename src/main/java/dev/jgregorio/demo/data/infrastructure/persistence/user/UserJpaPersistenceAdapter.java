package dev.jgregorio.demo.data.infrastructure.persistence.user;

import dev.jgregorio.demo.data.application.out.user.UserPersistencePort;
import dev.jgregorio.demo.data.domain.user.User;
import dev.jgregorio.demo.data.domain.user.UserSearch;
import dev.jgregorio.demo.data.infrastructure.persistence.GenericJpaPersistenceAdapter;
import dev.jgregorio.demo.data.infrastructure.persistence.user.search.UserSearchSpecification;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserJpaPersistenceAdapter extends GenericJpaPersistenceAdapter<User, UserEntity, Long>
        implements UserPersistencePort {

    protected UserJpaPersistenceAdapter(
            UserPersistenceMapper mapper, UserJpaPersistenceRepository repository) {
        super(mapper, repository, UserEntity::new, User::id);
    }

    public Optional<User> getByUsername(String username) {
        return getRepository().findByUsername(username).map(mapper::toDomain);
    }

    private UserJpaPersistenceRepository getRepository() {
        return (UserJpaPersistenceRepository) jpaRepository;
    }

    @Override
    public List<User> search(UserSearch criteria, Sort sort) {
        Specification<UserEntity> spec = UserSearchSpecification.search(criteria);
        List<UserEntity> entities = getRepository().findAll(spec, sort);
        return entities.stream().map(mapper::toDomain).toList();
    }
}
