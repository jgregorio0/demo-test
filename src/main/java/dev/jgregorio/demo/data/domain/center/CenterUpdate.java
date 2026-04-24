package dev.jgregorio.demo.data.domain.center;

import dev.jgregorio.demo.data.domain.location.Location;
import lombok.Builder;

@Builder(toBuilder = true)
public record CenterUpdate(
        Long id, Long clientId, String name, String address, String postalCode, Long locationId) {

    public Center toCenter(Center center) {
        return center.toBuilder()
                .id(id)
                .clientId(clientId)
                .name(name)
                .address(address)
                .postalCode(postalCode)
                .location(Location.from(locationId))
                .build();
    }
}
