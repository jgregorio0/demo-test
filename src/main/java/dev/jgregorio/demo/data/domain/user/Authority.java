package dev.jgregorio.demo.data.domain.user;

import lombok.Builder;

@Builder(toBuilder = true)
public record Authority(String authority) {
}
