package dev.jgregorio.demo.data.domain.center;

import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.CENTER_ADDRESS;
import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.CENTER_NAME;
import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.CENTER_POSTAL_CODE;
import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.CLIENT_ID;
import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.createCenter;
import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.createCenterCreation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jgregorio.demo.data.application.out.center.CenterPersistencePort;

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
                .should()
                .create(
                        argThat(
                                c -> c.clientId().equals(CLIENT_ID)
                                        && c.name().equals(CENTER_NAME)
                                        && c.address().equals(CENTER_ADDRESS)
                                        && c.postalCode().equals(CENTER_POSTAL_CODE)));
        assertThat(result).isNotNull();
    }
}
