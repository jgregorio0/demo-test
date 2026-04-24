package dev.jgregorio.demo.data.infrastructure.api.center.read;

import dev.jgregorio.demo.data.domain.center.CenterRead;
import dev.jgregorio.demo.data.infrastructure.api.GenericRequestApiMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CenterReadApiMapper
        extends GenericRequestApiMapper<CenterReadRequest, CenterRead> {
}
