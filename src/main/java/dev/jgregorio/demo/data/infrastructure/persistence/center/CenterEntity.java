package dev.jgregorio.demo.data.infrastructure.persistence.center;

import dev.jgregorio.demo.data.infrastructure.persistence.location.LocationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CENTERS", schema = "TEST")
@Getter
@Setter
public class CenterEntity {

    @EmbeddedId
    private CenterEntityId id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "POSTAL_CODE", length = 10)
    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID")
    private LocationEntity location;
}
