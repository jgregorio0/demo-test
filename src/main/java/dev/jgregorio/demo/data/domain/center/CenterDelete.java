package dev.jgregorio.demo.data.domain.center;

import lombok.Builder;

@Builder(toBuilder = true)
public record CenterDelete(Long id, Long clientId) {
}
