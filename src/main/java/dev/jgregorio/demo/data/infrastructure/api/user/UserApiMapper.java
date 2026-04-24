package dev.jgregorio.demo.data.infrastructure.api.user;

import dev.jgregorio.demo.data.domain.user.User;
import dev.jgregorio.demo.data.infrastructure.api.GenericResponseApiMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserApiMapper extends GenericResponseApiMapper<User, UserResponse> {
}
