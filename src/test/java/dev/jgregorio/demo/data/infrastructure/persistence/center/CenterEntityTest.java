package dev.jgregorio.demo.data.infrastructure.persistence.center;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import dev.jgregorio.demo.data.domain.center.Center;
import dev.jgregorio.demo.data.domain.location.Location;

@DataJpaTest
class CenterEntityTest {

    @Autowired
    private CenterJpaPersistenceRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findById should return center when exists")
    @Sql("/dev/jgregorio/demo/data/infrastructure/persistence/center/insert_center_1_client_2.sql")
    void findById_shouldReturnCenter_whenExists() {
        // Given
        Center center = Center.builder()
                .id(1L)
                .clientId(2L)
                .name("Center 1")
                .address("Address 1")
                .postalCode("12345")
                .location(Location.builder().id(3L).build())
                .build();

        // When
        CenterEntityId id = CenterEntityId.from(center.id(), center.clientId());
        var foundCenter = repository.findById(id);

        // Then
        assertThat(foundCenter.isPresent());
        assertThat(foundCenter.get().getId().getId()).isEqualTo(center.id());
        assertThat(foundCenter.get().getId().getClientId()).isEqualTo(center.clientId());
        assertThat(foundCenter.get().getName()).isEqualTo("Center 1");
        assertThat(foundCenter.get().getAddress()).isEqualTo("Address 1");
        assertThat(foundCenter.get().getPostalCode()).isEqualTo("12345");
        assertThat(foundCenter.get().getLocation()).isNotNull();
        assertThat(foundCenter.get().getLocation().getId()).isEqualTo(center.location().id());
    }
}
