package dev.jgregorio.demo.data.domain.center;

import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.CENTER_ID;
import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.CLIENT_ID;
import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.LOCATION_ID;
import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.createCenterUpdate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jgregorio.demo.data.application.out.center.CenterPersistencePort;
import dev.jgregorio.demo.data.domain.exception.ResourceNotFoundException;
import dev.jgregorio.demo.data.domain.location.Location;

@ExtendWith(MockitoExtension.class)
class UpdateCenterServiceTest {

        @Mock
        private CenterPersistencePort persistence;
        @InjectMocks
        private CenterService service;

        private CenterUpdate centerUpdate;

        @BeforeEach
        void setUp() {
                centerUpdate = createCenterUpdate();
        }

        @Test
        @DisplayName("update should read existing center, apply updates, and persist")
        void update_shouldReadCenterApplyUpdatesAndPersist() {
                // Given
                Center existingCenter = Center.builder()
                                .id(CENTER_ID)
                                .clientId(CLIENT_ID)
                                .name("Old Name")
                                .address("Old Address")
                                .postalCode("00000")
                                .location(Location.from(LOCATION_ID))
                                .build();

                Center updatedCenter = Center.builder()
                                .id(CENTER_ID)
                                .clientId(CLIENT_ID)
                                .name("Updated Name")
                                .address("Updated Address")
                                .postalCode("54321")
                                .location(Location.from(LOCATION_ID))
                                .build();

                when(persistence.read(CENTER_ID, CLIENT_ID)).thenReturn(Optional.of(existingCenter));
                when(persistence.update(any(Center.class))).thenReturn(updatedCenter);

                // When
                Center result = service.update(centerUpdate);

                // Then
                then(persistence).should().read(CENTER_ID, CLIENT_ID);
                then(persistence)
                                .should().update(
                                                argThat(
                                                                c -> c.id().equals(CENTER_ID)
                                                                                && c.clientId().equals(CLIENT_ID)
                                                                                && c.name().equals("Updated Name")
                                                                                && c.address().equals("Updated Address")
                                                                                && c.postalCode().equals("54321")));
                assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("update should throw ResourceNotFoundException when center not found")
        void update_shouldThrowResourceNotFoundException_whenCenterNotFound() {
                // Given
                when(persistence.read(CENTER_ID, CLIENT_ID)).thenReturn(Optional.empty());

                // When & Then
                assertThrows(ResourceNotFoundException.class, () -> service.update(centerUpdate));
                then(persistence).should().read(CENTER_ID, CLIENT_ID);
                then(persistence).should(never()).update(any(Center.class));
        }
}
