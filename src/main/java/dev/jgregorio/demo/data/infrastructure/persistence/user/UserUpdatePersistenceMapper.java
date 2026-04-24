package dev.jgregorio.demo.data.infrastructure.persistence.user;

import dev.jgregorio.demo.data.domain.user.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserUpdatePersistenceMapper {

    // Mappstruct cannot use constructor injection.
    @Autowired
    protected UserJpaPersistenceRepository userEntityJpaRepository;
    @Autowired
    protected UserPersistenceMapper userMapper;

    public User toDomain(UserEntity entity) {
        return userMapper.toDomain(entity);
    }

    public UserEntity toEntity(User domain) {
        if (domain == null || domain.id() == null) {
            return null;
        }
        return userEntityJpaRepository.getReferenceById(domain.id());
    }
}
