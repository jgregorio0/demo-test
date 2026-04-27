package dev.jgregorio.demo.data.infrastructure.api.center;

import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.CENTER_ADDRESS;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.CENTER_NAME;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.CENTER_POSTAL_CODE;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.CLIENT_ID;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.LOCATION_ID;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.createCenter;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.createCenterCreation;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.createCenterCreationRequest;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.createCenterResponse;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.jgregorio.demo.data.application.in.center.CenterUseCase;
import dev.jgregorio.demo.data.domain.center.Center;
import dev.jgregorio.demo.data.domain.center.CenterCreation;
import dev.jgregorio.demo.data.infrastructure.api.center.create.CenterCreationApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.create.CenterCreationRequest;
import dev.jgregorio.demo.data.infrastructure.api.center.delete.CenterDeleteApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.read.CenterReadApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.search.CenterSearchApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.update.CenterUpdateApiMapper;

@WebMvcTest(CenterController.class)
class CreateCenterControllerTest {

    @MockitoBean
    private CenterUseCase centerService;
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
    @MockitoBean
    private CenterSearchApiMapper searchMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should create a new center when request is valid")
    void create_shouldCreateCenter_whenRequestIsValid() throws Exception {
        // Given
        CenterCreationRequest createRequest = createCenterCreationRequest();
        CenterCreation centerCreation = createCenterCreation();
        Center createdCenter = createCenter();
        CenterResponse centerResponse = createCenterResponse();

        when(creationMapper.fromRequest(any(CenterCreationRequest.class))).thenReturn(centerCreation);
        when(centerService.create(any(CenterCreation.class))).thenReturn(createdCenter);
        when(mapper.toResponse(createdCenter)).thenReturn(centerResponse);

        // When & Then
        mockMvc
                .perform(
                        post("/centers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper
                                        .writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(createdCenter.id().intValue())))
                .andExpect(jsonPath("$.clientId", is(createdCenter.clientId().intValue())))
                .andExpect(jsonPath("$.name", is(createRequest.name())))
                .andExpect(jsonPath("$.address", is(createRequest.address())))
                .andExpect(jsonPath("$.postalCode", is(createRequest.postalCode())));

        then(creationMapper).should().fromRequest(createRequest);
        then(centerService).should().create(centerCreation);
        then(mapper).should().toResponse(createdCenter);
    }

    @Test
    @DisplayName("should return 400 when clientId is null")
    void create_shouldReturnBadRequest_whenClientIdIsNull() throws Exception {
        // Given
        CenterCreationRequest invalidRequest = CenterCreationRequest.builder()
                .clientId(null)
                .name(CENTER_NAME)
                .address(CENTER_ADDRESS)
                .postalCode(CENTER_POSTAL_CODE)
                .locationId(LOCATION_ID)
                .build();

        // When & Then
        mockMvc
                .perform(
                        post("/centers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper
                                        .writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        then(centerService).should(never()).create(any(CenterCreation.class));
    }

    @Test
    @DisplayName("should return 400 when name is null")
    void create_shouldReturnBadRequest_whenNameIsNull() throws Exception {
        // Given
        CenterCreationRequest invalidRequest = CenterCreationRequest.builder()
                .clientId(CLIENT_ID)
                .name(null)
                .address(CENTER_ADDRESS)
                .postalCode(CENTER_POSTAL_CODE)
                .locationId(LOCATION_ID)
                .build();

        // When & Then
        mockMvc
                .perform(
                        post("/centers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper
                                        .writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        then(centerService).should(never()).create(any(CenterCreation.class));
    }

    @Test
    @DisplayName("should return 400 when name is blank")
    void create_shouldReturnBadRequest_whenNameIsBlank() throws Exception {
        // Given
        CenterCreationRequest invalidRequest = CenterCreationRequest.builder()
                .clientId(CLIENT_ID)
                .name("   ")
                .address(CENTER_ADDRESS)
                .postalCode(CENTER_POSTAL_CODE)
                .locationId(LOCATION_ID)
                .build();

        // When & Then
        mockMvc
                .perform(
                        post("/centers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper
                                        .writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        then(centerService).should(never()).create(any(CenterCreation.class));
    }

    @Test
    @DisplayName("should return 400 when address is null")
    void create_shouldReturnBadRequest_whenAddressIsNull() throws Exception {
        // Given
        CenterCreationRequest invalidRequest = CenterCreationRequest.builder()
                .clientId(CLIENT_ID)
                .name(CENTER_NAME)
                .address(null)
                .postalCode(CENTER_POSTAL_CODE)
                .locationId(LOCATION_ID)
                .build();

        // When & Then
        mockMvc
                .perform(
                        post("/centers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper
                                        .writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        then(centerService).should(never()).create(any(CenterCreation.class));
    }

    @Test
    @DisplayName("should return 400 when address is blank")
    void create_shouldReturnBadRequest_whenAddressIsBlank() throws Exception {
        // Given
        CenterCreationRequest invalidRequest = CenterCreationRequest.builder()
                .clientId(CLIENT_ID)
                .name(CENTER_NAME)
                .address("")
                .postalCode(CENTER_POSTAL_CODE)
                .locationId(LOCATION_ID)
                .build();

        // When & Then
        mockMvc
                .perform(
                        post("/centers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper
                                        .writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        then(centerService).should(never()).create(any(CenterCreation.class));
    }

    @Test
    @DisplayName("should return 400 when postalCode is null")
    void create_shouldReturnBadRequest_whenPostalCodeIsNull() throws Exception {
        // Given
        CenterCreationRequest invalidRequest = CenterCreationRequest.builder()
                .clientId(CLIENT_ID)
                .name(CENTER_NAME)
                .address(CENTER_ADDRESS)
                .postalCode(null)
                .locationId(LOCATION_ID)
                .build();

        // When & Then
        mockMvc
                .perform(
                        post("/centers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper
                                        .writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        then(centerService).should(never()).create(any(CenterCreation.class));
    }

    @Test
    @DisplayName("should return 400 when postalCode is blank")
    void create_shouldReturnBadRequest_whenPostalCodeIsBlank() throws Exception {
        // Given
        CenterCreationRequest invalidRequest = CenterCreationRequest.builder()
                .clientId(CLIENT_ID)
                .name(CENTER_NAME)
                .address(CENTER_ADDRESS)
                .postalCode("  ")
                .locationId(LOCATION_ID)
                .build();

        // When & Then
        mockMvc
                .perform(
                        post("/centers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper
                                        .writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        then(centerService).should(never()).create(any(CenterCreation.class));
    }

    @Test
    @DisplayName("should return 400 when locationId is null")
    void create_shouldReturnBadRequest_whenLocationIdIsNull() throws Exception {
        // Given
        CenterCreationRequest invalidRequest = CenterCreationRequest.builder()
                .clientId(CLIENT_ID)
                .name(CENTER_NAME)
                .address(CENTER_ADDRESS)
                .postalCode(CENTER_POSTAL_CODE)
                .locationId(null)
                .build();

        // When & Then
        mockMvc
                .perform(
                        post("/centers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper
                                        .writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        then(centerService).should(never()).create(any(CenterCreation.class));
    }

    @Test
    @DisplayName("should return 400 when request body is empty")
    void create_shouldReturnBadRequest_whenRequestBodyIsEmpty() throws Exception {
        // When & Then
        mockMvc
                .perform(post("/centers").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());

        then(centerService).should(never()).create(any(CenterCreation.class));
    }

    @Test
    @DisplayName("should return 400 when content type is not JSON")
    void create_shouldReturnBadRequest_whenContentTypeIsNotJson() throws Exception {
        // Given
        CenterCreationRequest createRequest = createCenterCreationRequest();

        // When & Then
        mockMvc
                .perform(
                        post("/centers")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content(objectMapper
                                        .writeValueAsString(createRequest)))
                .andExpect(status().isUnsupportedMediaType());

        then(centerService).should(never()).create(any(CenterCreation.class));
    }
}
