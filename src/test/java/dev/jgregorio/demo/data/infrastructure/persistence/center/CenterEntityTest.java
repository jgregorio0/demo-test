package dev.jgregorio.demo.data.infrastructure.persistence.center;

import dev.jgregorio.demo.data.domain.center.Center;
import dev.jgregorio.demo.data.domain.location.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

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
        Center center =
                Center.builder()
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
        assertTrue(foundCenter.isPresent());
        assertEquals(center.id(), foundCenter.get().getId().getId());
        assertEquals(center.clientId(), foundCenter.get().getId().getClientId());
        assertEquals("Center 1", foundCenter.get().getName());
        assertEquals("Address 1", foundCenter.get().getAddress());
        assertEquals("12345", foundCenter.get().getPostalCode());
        assertNotNull(foundCenter.get().getLocation());
        assertEquals(center.location().id(), foundCenter.get().getLocation().getId());
    }
}
