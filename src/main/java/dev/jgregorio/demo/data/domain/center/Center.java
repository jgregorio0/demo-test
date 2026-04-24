package dev.jgregorio.demo.data.domain.center;

import dev.jgregorio.demo.data.domain.location.Location;
import lombok.Builder;

@Builder(toBuilder = true)
public record Center(
        Long id, Long clientId, String name, String address, String postalCode, Location location) {
}
