package dev.jgregorio.demo.data.infrastructure.api.center.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.jgregorio.demo.data.domain.location.Location;
import lombok.Builder;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CenterSearchResponse(
        Long id, Long clientId, String name, String address, String postalCode, Location location) {
}
