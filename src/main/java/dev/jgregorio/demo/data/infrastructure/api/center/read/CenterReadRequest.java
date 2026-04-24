package dev.jgregorio.demo.data.infrastructure.api.center.read;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CenterReadRequest(@NotNull Long id, @NotNull Long clientId) {
}
