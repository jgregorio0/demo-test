package dev.jgregorio.demo.data.domain.center;

import dev.jgregorio.demo.data.application.out.center.CenterPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class CreateCenterServiceTest {

    private static final Long NEXT_ID = 10L;

    @Mock
    private CenterPersistencePort persistence;
    @InjectMocks
    private CenterService service;

    private CenterCreation centerCreation;
    private Center center;

    @BeforeEach
    void setUp() {
        centerCreation = createCenterCreation();
        center = createCenter();
    }

    @Test
    @DisplayName("create should get next ID, convert creation to center, and persist it")
    void create_shouldGetNextIdAndPersistCenter() {
        // Given
        when(persistence.create(any(Center.class))).thenReturn(center);

        // When
        Center result = service.create(centerCreation);

        // Then
        then(persistence)
                .create(
                        argThat(
                                c ->
                                        c.clientId().equals(CLIENT_ID)
                                                && c.name().equals(CENTER_NAME)
                                                && c.address().equals(CENTER_ADDRESS)
                                                && c.postalCode().equals(CENTER_POSTAL_CODE)));
        assertThat(result).should().isNotNull();
    }
}
