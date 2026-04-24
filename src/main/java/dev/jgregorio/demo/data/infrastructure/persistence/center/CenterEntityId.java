package dev.jgregorio.demo.data.infrastructure.persistence.center;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CenterEntityId {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CENTERS_SEQ")
    @SequenceGenerator(name = "CENTERS_SEQ", sequenceName = "TEST.CENTERS_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "CLIENT_ID", nullable = false)
    private Long clientId;

    public static CenterEntityId from(Long id, Long clientId) {
        return new CenterEntityId(id, clientId);
    }
}
