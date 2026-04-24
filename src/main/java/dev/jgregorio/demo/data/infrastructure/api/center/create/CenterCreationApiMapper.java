package dev.jgregorio.demo.data.infrastructure.api.center.create;

import dev.jgregorio.demo.data.domain.center.CenterCreation;
import dev.jgregorio.demo.data.infrastructure.api.GenericRequestApiMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CenterCreationApiMapper
        extends GenericRequestApiMapper<CenterCreationRequest, CenterCreation> {
}
