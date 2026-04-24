package dev.jgregorio.demo.data.infrastructure.api.center.update;

import dev.jgregorio.demo.data.domain.center.CenterUpdate;
import dev.jgregorio.demo.data.infrastructure.api.GenericRequestApiMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CenterUpdateApiMapper
        extends GenericRequestApiMapper<CenterUpdateRequest, CenterUpdate> {
}
