package dev.jgregorio.demo.data.infrastructure.persistence.location;

import dev.jgregorio.demo.data.domain.location.Location;
import dev.jgregorio.demo.data.infrastructure.persistence.GenericPersistenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationPersistenceMapper
        extends GenericPersistenceMapper<Location, LocationEntity> {

}
