package dev.jgregorio.demo.data.infrastructure.persistence.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "USERS", schema = "TEST")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
    @SequenceGenerator(name = "USERS_SEQ", sequenceName = "TEST.USERS_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "ENABLED")
    @Convert(converter = NumericBooleanConverter.class)
    private Boolean enabled;

    @Column(name = "PASSWORD_RESET_DATE")
    private LocalDateTime lastPasswordResetDate;
}
