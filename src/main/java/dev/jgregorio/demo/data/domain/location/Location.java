package dev.jgregorio.demo.data.domain.location;

import lombok.Builder;

@Builder(toBuilder = true)
public record Location(Long id, String name) {

    public static Location from(Long locationId) {
        return Location.builder().id(locationId).build();
    }
}
