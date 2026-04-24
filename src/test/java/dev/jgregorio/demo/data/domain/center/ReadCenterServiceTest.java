package dev.jgregorio.demo.data.domain.center;

import dev.jgregorio.demo.data.application.out.center.CenterPersistencePort;
import dev.jgregorio.demo.data.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadCenterServiceTest {

    @Mock
    private CenterPersistencePort persistence;
    @InjectMocks
    private CenterService service;

    private CenterRead centerRead;
    private Center center;

    @BeforeEach
    void setUp() {
        centerRead = createCenterRead();
        center = createCenter();
    }

    @Test
    @DisplayName("read should call persistence with correct id and clientId")
    void read_shouldCallPersistenceWithCorrectParameters() {
        // Given
        when(persistence.read(CENTER_ID, CLIENT_ID)).thenReturn(Optional.of(center));

        // When
        Center result = service.read(centerRead);

        // Then
        then(persistence).should().read(CENTER_ID, CLIENT_ID);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("read should throw ResourceNotFoundException when center not found")
    void read_shouldThrowResourceNotFoundException_whenCenterNotFound() {
        // Given
        when(persistence.read(CENTER_ID, CLIENT_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> service.read(centerRead));
        then(persistence).should().read(CENTER_ID, CLIENT_ID);
    }
}
