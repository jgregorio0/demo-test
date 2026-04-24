package dev.jgregorio.demo.data.domain.center;

import lombok.Builder;

@Builder(toBuilder = true)
public record CenterSearch(String name, String address, String postalCode) {
}
