package dev.jgregorio.demo.data.infrastructure.api.center.search;

import dev.jgregorio.demo.data.domain.center.Center;
import dev.jgregorio.demo.data.domain.center.CenterSearch;
import dev.jgregorio.demo.data.infrastructure.api.GenericRequestApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.GenericResponseApiMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CenterSearchApiMapper
        extends GenericRequestApiMapper<CenterSearchRequest, CenterSearch>,
        GenericResponseApiMapper<Center, CenterSearchResponse> {
}
