package dev.jgregorio.demo.data.infrastructure.persistence.user;

import dev.jgregorio.demo.data.domain.user.User;
import dev.jgregorio.demo.data.infrastructure.persistence.GenericPersistenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserPersistenceMapper extends GenericPersistenceMapper<User, UserEntity> {

    @Override
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toDomain(UserEntity entity);

}
