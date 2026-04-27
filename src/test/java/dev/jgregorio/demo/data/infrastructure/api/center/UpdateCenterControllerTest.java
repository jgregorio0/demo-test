package dev.jgregorio.demo.data.infrastructure.api.center;

import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.CENTER_ADDRESS;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.CENTER_ID;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.CENTER_NAME;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.CENTER_POSTAL_CODE;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.CLIENT_ID;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.LOCATION_ID;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.createCenter;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.createCenterResponse;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.createCenterUpdate;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.createCenterUpdateRequest;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import dev.jgregorio.demo.data.domain.center.CenterUpdate;
import dev.jgregorio.demo.data.infrastructure.api.center.create.CenterCreationApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.delete.CenterDeleteApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.read.CenterReadApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.search.CenterSearchApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.update.CenterUpdateApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.update.CenterUpdateRequest;

@WebMvcTest(CenterController.class)
class UpdateCenterControllerTest {

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
        @DisplayName("should update an existing center when request is valid")
        void update_shouldUpdateCenter_whenRequestIsValid() throws Exception {
                // Given
                CenterUpdateRequest updateRequest = createCenterUpdateRequest();
                CenterUpdate centerUpdate = createCenterUpdate();
                Center updatedCenter = createCenter();
                CenterResponse centerResponse = createCenterResponse();

                given(updateMapper.fromRequest(any(CenterUpdateRequest.class))).willReturn(centerUpdate);
                given(centerService.update(centerUpdate)).willReturn(updatedCenter);
                given(mapper.toResponse(updatedCenter)).willReturn(centerResponse);

                // When & Then
                mockMvc
                                .perform(
                                                put("/centers")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper
                                                                                .writeValueAsString(updateRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(updatedCenter.id().intValue())))
                                .andExpect(jsonPath("$.clientId", is(updatedCenter.clientId().intValue())))
                                .andExpect(jsonPath("$.name", is(updatedCenter.name())))
                                .andExpect(jsonPath("$.address", is(updatedCenter.address())))
                                .andExpect(jsonPath("$.postalCode", is(updatedCenter.postalCode())))
                                .andExpect(jsonPath("$.location.id", is(updatedCenter.location().id().intValue())));

                then(updateMapper).should().fromRequest(any(CenterUpdateRequest.class));
                then(centerService).should().update(centerUpdate);
                then(mapper).should().toResponse(updatedCenter);
        }

        @Test
        @DisplayName("should return 400 when id is null")
        void update_shouldReturnBadRequest_whenIdIsNull() throws Exception {
                // Given
                CenterUpdateRequest invalidRequest = CenterUpdateRequest.builder()
                                .id(null)
                                .clientId(CLIENT_ID)
                                .name(CENTER_NAME)
                                .address(CENTER_ADDRESS)
                                .postalCode(CENTER_POSTAL_CODE)
                                .locationId(LOCATION_ID)
                                .build();

                // When & Then
                mockMvc
                                .perform(
                                                put("/centers")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper
                                                                                .writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());

                then(centerService).should(never()).update(any(CenterUpdate.class));
        }

        @Test
        @DisplayName("should return 400 when clientId is null")
        void update_shouldReturnBadRequest_whenClientIdIsNull() throws Exception {
                // Given
                CenterUpdateRequest invalidRequest = CenterUpdateRequest.builder()
                                .id(CENTER_ID)
                                .clientId(null)
                                .name(CENTER_NAME)
                                .address(CENTER_ADDRESS)
                                .postalCode(CENTER_POSTAL_CODE)
                                .locationId(LOCATION_ID)
                                .build();

                // When & Then
                mockMvc
                                .perform(
                                                put("/centers")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper
                                                                                .writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());

                then(centerService).should(never()).update(any(CenterUpdate.class));
        }

        @Test
        @DisplayName("should return 400 when name is null")
        void update_shouldReturnBadRequest_whenNameIsNull() throws Exception {
                // Given
                CenterUpdateRequest invalidRequest = CenterUpdateRequest.builder()
                                .id(CENTER_ID)
                                .clientId(CLIENT_ID)
                                .name(null)
                                .address(CENTER_ADDRESS)
                                .postalCode(CENTER_POSTAL_CODE)
                                .locationId(LOCATION_ID)
                                .build();

                // When & Then
                mockMvc
                                .perform(
                                                put("/centers")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper
                                                                                .writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());

                then(centerService).should(never()).update(any(CenterUpdate.class));
        }

        @Test
        @DisplayName("should return 400 when name is blank")
        void update_shouldReturnBadRequest_whenNameIsBlank() throws Exception {
                // Given
                CenterUpdateRequest invalidRequest = CenterUpdateRequest.builder()
                                .id(CENTER_ID)
                                .clientId(CLIENT_ID)
                                .name("   ")
                                .address(CENTER_ADDRESS)
                                .postalCode(CENTER_POSTAL_CODE)
                                .locationId(LOCATION_ID)
                                .build();

                // When & Then
                mockMvc
                                .perform(
                                                put("/centers")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper
                                                                                .writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());

                then(centerService).should(never()).update(any(CenterUpdate.class));
        }

        @Test
        @DisplayName("should return 400 when address is null")
        void update_shouldReturnBadRequest_whenAddressIsNull() throws Exception {
                // Given
                CenterUpdateRequest invalidRequest = CenterUpdateRequest.builder()
                                .id(CENTER_ID)
                                .clientId(CLIENT_ID)
                                .name(CENTER_NAME)
                                .address(null)
                                .postalCode(CENTER_POSTAL_CODE)
                                .locationId(LOCATION_ID)
                                .build();

                // When & Then
                mockMvc
                                .perform(
                                                put("/centers")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper
                                                                                .writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());

                then(centerService).should(never()).update(any(CenterUpdate.class));
        }

        @Test
        @DisplayName("should return 400 when address is blank")
        void update_shouldReturnBadRequest_whenAddressIsBlank() throws Exception {
                // Given
                CenterUpdateRequest invalidRequest = CenterUpdateRequest.builder()
                                .id(CENTER_ID)
                                .clientId(CLIENT_ID)
                                .name(CENTER_NAME)
                                .address("")
                                .postalCode(CENTER_POSTAL_CODE)
                                .locationId(LOCATION_ID)
                                .build();

                // When & Then
                mockMvc
                                .perform(
                                                put("/centers")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper
                                                                                .writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());

                then(centerService).should(never()).update(any(CenterUpdate.class));
        }

        @Test
        @DisplayName("should return 400 when postalCode is null")
        void update_shouldReturnBadRequest_whenPostalCodeIsNull() throws Exception {
                // Given
                CenterUpdateRequest invalidRequest = CenterUpdateRequest.builder()
                                .id(CENTER_ID)
                                .clientId(CLIENT_ID)
                                .name(CENTER_NAME)
                                .address(CENTER_ADDRESS)
                                .postalCode(null)
                                .locationId(LOCATION_ID)
                                .build();

                // When & Then
                mockMvc
                                .perform(
                                                put("/centers")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper
                                                                                .writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());

                then(centerService).should(never()).update(any(CenterUpdate.class));
        }

        @Test
        @DisplayName("should return 400 when postalCode is blank")
        void update_shouldReturnBadRequest_whenPostalCodeIsBlank() throws Exception {
                // Given
                CenterUpdateRequest invalidRequest = CenterUpdateRequest.builder()
                                .id(CENTER_ID)
                                .clientId(CLIENT_ID)
                                .name(CENTER_NAME)
                                .address(CENTER_ADDRESS)
                                .postalCode("  ")
                                .locationId(LOCATION_ID)
                                .build();

                // When & Then
                mockMvc
                                .perform(
                                                put("/centers")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper
                                                                                .writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());

                then(centerService).should(never()).update(any(CenterUpdate.class));
        }

        @Test
        @DisplayName("should return 400 when locationId is null")
        void update_shouldReturnBadRequest_whenLocationIdIsNull() throws Exception {
                // Given
                CenterUpdateRequest invalidRequest = CenterUpdateRequest.builder()
                                .id(CENTER_ID)
                                .clientId(CLIENT_ID)
                                .name(CENTER_NAME)
                                .address(CENTER_ADDRESS)
                                .postalCode(CENTER_POSTAL_CODE)
                                .locationId(null)
                                .build();

                // When & Then
                mockMvc
                                .perform(
                                                put("/centers")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper
                                                                                .writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());

                then(centerService).should(never()).update(any(CenterUpdate.class));
        }

        @Test
        @DisplayName("should return 400 when request body is empty")
        void update_shouldReturnBadRequest_whenRequestBodyIsEmpty() throws Exception {
                // When & Then
                mockMvc
                                .perform(put("/centers").contentType(MediaType.APPLICATION_JSON).content("{}"))
                                .andExpect(status().isBadRequest());

                then(centerService).should(never()).update(any(CenterUpdate.class));
        }

        @Test
        @DisplayName("should return 400 when content type is not JSON")
        void update_shouldReturnBadRequest_whenContentTypeIsNotJson() throws Exception {
                // Given
                CenterUpdateRequest updateRequest = createCenterUpdateRequest();

                // When & Then
                mockMvc
                                .perform(
                                                put("/centers")
                                                                .contentType(MediaType.TEXT_PLAIN)
                                                                .content(objectMapper
                                                                                .writeValueAsString(updateRequest)))
                                .andExpect(status().isUnsupportedMediaType());

                then(centerService).should(never()).update(any(CenterUpdate.class));
        }
}
