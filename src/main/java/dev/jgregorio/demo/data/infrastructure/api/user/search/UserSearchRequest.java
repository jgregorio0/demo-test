package dev.jgregorio.demo.data.infrastructure.api.user.search;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserSearchRequest(@NotBlank String fullName) {
}
