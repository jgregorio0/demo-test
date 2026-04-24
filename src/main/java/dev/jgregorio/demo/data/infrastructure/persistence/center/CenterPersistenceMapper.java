package dev.jgregorio.demo.data.infrastructure.persistence.center;

import dev.jgregorio.demo.data.domain.center.Center;
import dev.jgregorio.demo.data.infrastructure.persistence.GenericPersistenceMapper;
import dev.jgregorio.demo.data.infrastructure.persistence.location.LocationPersistenceMapper;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {LocationPersistenceMapper.class})
public interface CenterPersistenceMapper extends GenericPersistenceMapper<Center, CenterEntity> {

    @Override
    @Mapping(target = "id", source = "id.id")
    @Mapping(target = "clientId", source = "id.clientId")
    Center toDomain(CenterEntity entity);

    @Override
    @Mapping(target = "id.id", source = "id")
    @Mapping(target = "id.clientId", source = "clientId")
    CenterEntity toEntity(Center domain);

    @Override
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id.id", source = "id")
    @Mapping(target = "id.clientId", source = "clientId")
    @Mapping(target = "name")
    @Mapping(target = "address")
    @Mapping(target = "postalCode")
    @Mapping(target = "location.id", source = "location.id")
    void update(@MappingTarget CenterEntity entity, Center domain);
}
