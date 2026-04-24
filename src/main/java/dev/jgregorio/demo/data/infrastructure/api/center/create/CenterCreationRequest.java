package dev.jgregorio.demo.data.infrastructure.api.center.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CenterCreationRequest(
        @NotNull Long clientId,
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank String postalCode,
        @NotNull Long locationId) {
}
