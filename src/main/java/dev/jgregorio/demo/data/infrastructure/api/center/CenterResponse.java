package dev.jgregorio.demo.data.infrastructure.api.center;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.jgregorio.demo.data.domain.location.Location;

import java.net.URI;

import lombok.Builder;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CenterResponse(
        Long id, Long clientId, String name, String address, String postalCode, Location location) {

    private static final String CREATED_LOCATION_FORMAT = "/clients/%s/centers/%s";

    public URI locationUri() {
        String location = String.format(CREATED_LOCATION_FORMAT, clientId, id);
        return URI.create(location);
    }
}
