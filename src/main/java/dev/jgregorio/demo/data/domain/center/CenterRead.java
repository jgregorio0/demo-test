package dev.jgregorio.demo.data.domain.center;

import lombok.Builder;

@Builder(toBuilder = true)
public record CenterRead(Long id, Long clientId) {
}
