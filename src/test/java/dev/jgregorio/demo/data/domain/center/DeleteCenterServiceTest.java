package dev.jgregorio.demo.data.domain.center;

import dev.jgregorio.demo.data.application.out.center.CenterPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteCenterServiceTest {

    @Mock
    private CenterPersistencePort persistence;
    @InjectMocks
    private CenterService service;

    @Test
    @DisplayName("delete should call persistence with correct id and clientId")
    void delete_shouldCallPersistenceWithCorrectParameters() {
        // Given
        CenterDelete centerDelete = createCenterDelete();
        doNothing().when(persistence).delete(CENTER_ID, CLIENT_ID);

        // When
        service.delete(centerDelete);

        // Then
        then(persistence).should().delete(CENTER_ID, CLIENT_ID);
    }
}
