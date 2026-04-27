package dev.jgregorio.demo.data.domain.center;

import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.CENTER_ID;
import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.CLIENT_ID;
import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.createCenterDelete;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jgregorio.demo.data.application.out.center.CenterPersistencePort;

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
