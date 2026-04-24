package dev.jgregorio.demo.data.infrastructure.api.center;

import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jgregorio.demo.data.application.in.center.CenterUseCase;
import dev.jgregorio.demo.data.domain.center.Center;
import dev.jgregorio.demo.data.domain.center.CenterRead;
import dev.jgregorio.demo.data.infrastructure.api.center.create.CenterCreationApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.delete.CenterDeleteApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.read.CenterReadApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.read.CenterReadRequest;
import dev.jgregorio.demo.data.infrastructure.api.center.search.CenterSearchApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.update.CenterUpdateApiMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CenterController.class)
class ReadCenterControllerTest {

    @MockitoBean
    private CenterUseCase centerService;
    @MockitoBean
    private CenterSearchApiMapper searchMapper;
    @MockitoBean
    private CenterApiMapper mapper;
    @MockitoBean
    private CenterCreationApiMapper creationMapper;
    @MockitoBean
    private CenterReadApiMapper readMapper;
    @MockitoBean
    private CenterUpdateApiMapper updateMapper;
    @MockitoBean
    private CenterDeleteApiMapper deleteMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should get center when id and clientId are valid")
    void get_shouldFindCenter_whenRequestIsValid() throws Exception {
        // Given
        CenterReadRequest readRequest = createCenterReadRequest();
        CenterRead centerRead = createCenterRead();
        Center center = createCenter();
        CenterResponse centerResponse = createCenterResponse();

        when(readMapper.fromRequest(any(CenterReadRequest.class))).thenReturn(centerRead);
        when(centerService.read(centerRead)).thenReturn(center);
        when(mapper.toResponse(center)).thenReturn(centerResponse);

        // When & Then
        mockMvc
                .perform(
                        get("/centers")
                                .param("id", String.valueOf(readRequest.id()))
                                .param("clientId", String.valueOf(readRequest.clientId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(centerResponse.id().intValue())))
                .andExpect(jsonPath("$.clientId", is(centerResponse.clientId().intValue())))
                .andExpect(jsonPath("$.name", is(centerResponse.name())))
                .andExpect(jsonPath("$.address", is(centerResponse.address())))
                .andExpect(jsonPath("$.postalCode", is(centerResponse.postalCode())))
                .andExpect(jsonPath("$.location.id", is(centerResponse.location().id().intValue())));

        then(readMapper).fromRequest(any(CenterReadRequest.class));
        verify(centerService).read(centerRead);
        verify(mapper).toResponse(center);
    }

    @Test
    @DisplayName("should return 400 when id is null")
    void get_shouldReturnBadRequest_whenIdIsNull() throws Exception {
        // When & Then
        mockMvc
                .perform(get("/centers").should().param("clientId", String.valueOf(CLIENT_ID)))
                .andExpect(status().isBadRequest());

        then(centerService).should(never()).read(any(CenterRead.class));
    }

    @Test
    @DisplayName("should return 400 when clientId is null")
    void get_shouldReturnBadRequest_whenClientIdIsNull() throws Exception {
        // When & Then
        mockMvc
                .perform(get("/centers").param("id", String.valueOf(CENTER_ID)))
                .andExpect(status().isBadRequest());

        then(centerService).should(never()).read(any(CenterRead.class));
    }

    @Test
    @DisplayName("should return 400 when id is not a valid number")
    void get_shouldReturnBadRequest_whenIdIsNotANumber() throws Exception {
        // When & Then
        mockMvc
                .perform(
                        get("/centers").param("id", "invalid").param("clientId", String.valueOf(CLIENT_ID)))
                .andExpect(status().isBadRequest());

        then(centerService).should(never()).read(any(CenterRead.class));
    }

    @Test
    @DisplayName("should return 400 when clientId is not a valid number")
    void get_shouldReturnBadRequest_whenClientIdIsNotANumber() throws Exception {
        // When & Then
        mockMvc
                .perform(
                        get("/centers").param("id", String.valueOf(CENTER_ID)).param("clientId", "invalid"))
                .andExpect(status().isBadRequest());

        then(centerService).should(never()).read(any(CenterRead.class));
    }
}
