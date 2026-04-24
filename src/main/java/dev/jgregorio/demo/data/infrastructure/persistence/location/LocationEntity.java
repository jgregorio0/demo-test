package dev.jgregorio.demo.data.infrastructure.persistence.location;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "LOCATIONS", schema = "TEST")
@Getter
@Setter
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCATIONS_SEQ")
    @SequenceGenerator(
            name = "LOCATIONS_SEQ",
            sequenceName = "TEST.LOCATIONS_SEQ",
            allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME")
    private String name;
}
