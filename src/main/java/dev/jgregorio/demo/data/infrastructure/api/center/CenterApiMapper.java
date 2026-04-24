package dev.jgregorio.demo.data.infrastructure.api.center;

import dev.jgregorio.demo.data.domain.center.Center;
import dev.jgregorio.demo.data.infrastructure.api.GenericResponseApiMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CenterApiMapper extends GenericResponseApiMapper<Center, CenterResponse> {
}
