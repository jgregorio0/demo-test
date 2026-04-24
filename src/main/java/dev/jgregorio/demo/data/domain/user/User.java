package dev.jgregorio.demo.data.domain.user;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import org.springframework.lang.Nullable;

@Builder(toBuilder = true)
public record User(
        Long id,
        String username,
        String name,
        String surname,
        String password,
        String email,
        boolean enabled,
        LocalDateTime lastPasswordResetDate,
        List<Authority> authorities)
        implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static User EMPTY = User.builder().build();

    public static User empty() {
        return EMPTY;
    }

    @Nullable
    public static User from(@Nullable Long id) {
        if (id == null) {
            return null;
        }
        return User.builder().id(id).build();
    }
}
