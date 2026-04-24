package dev.jgregorio.demo.data.infrastructure.api.center.search;

import lombok.Builder;

@Builder
public record CenterSearchRequest(String name, String address, String postalCode) {
}
