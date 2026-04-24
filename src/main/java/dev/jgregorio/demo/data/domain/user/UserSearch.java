package dev.jgregorio.demo.data.domain.user;

import java.io.Serial;
import java.io.Serializable;

import lombok.Builder;

@Builder(toBuilder = true)
public record UserSearch(String fullName) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
