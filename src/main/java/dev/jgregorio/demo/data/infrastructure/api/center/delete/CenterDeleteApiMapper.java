package dev.jgregorio.demo.data.infrastructure.api.center.delete;

import dev.jgregorio.demo.data.domain.center.CenterDelete;
import dev.jgregorio.demo.data.infrastructure.api.GenericRequestApiMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CenterDeleteApiMapper
        extends GenericRequestApiMapper<CenterDeleteRequest, CenterDelete> {
}
