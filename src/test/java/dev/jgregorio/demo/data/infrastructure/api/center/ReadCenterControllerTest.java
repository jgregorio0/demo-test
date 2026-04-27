package dev.jgregorio.demo.data.infrastructure.api.center;

import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.CENTER_ID;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.CLIENT_ID;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.createCenter;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.createCenterRead;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.createCenterReadRequest;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.createCenterResponse;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

                given(readMapper.fromRequest(any(CenterReadRequest.class))).willReturn(centerRead);
                given(centerService.read(centerRead)).willReturn(center);
                given(mapper.toResponse(center)).willReturn(centerResponse);

                // When & Then
                mockMvc
                                .perform(
                                                get("/centers")
                                                                .param("id", String.valueOf(readRequest.id()))
                                                                .param("clientId",
                                                                                String.valueOf(readRequest.clientId())))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(centerResponse.id().intValue())))
                                .andExpect(jsonPath("$.clientId", is(centerResponse.clientId().intValue())))
                                .andExpect(jsonPath("$.name", is(centerResponse.name())))
                                .andExpect(jsonPath("$.address", is(centerResponse.address())))
                                .andExpect(jsonPath("$.postalCode", is(centerResponse.postalCode())))
                                .andExpect(jsonPath("$.location.id", is(centerResponse.location().id().intValue())));

                then(readMapper).should().fromRequest(readRequest);
                then(centerService).should().read(centerRead);
                then(mapper).should().toResponse(center);
        }

        @Test
        @DisplayName("should return 400 when id is null")
        void get_shouldReturnBadRequest_whenIdIsNull() throws Exception {
                // When & Then
                mockMvc
                                .perform(get("/centers").param("clientId", String.valueOf(CLIENT_ID)))
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
                                                get("/centers").param("id", "invalid").param("clientId",
                                                                String.valueOf(CLIENT_ID)))
                                .andExpect(status().isBadRequest());

                then(centerService).should(never()).read(any(CenterRead.class));
        }

        @Test
        @DisplayName("should return 400 when clientId is not a valid number")
        void get_shouldReturnBadRequest_whenClientIdIsNotANumber() throws Exception {
                // When & Then
                mockMvc
                                .perform(
                                                get("/centers").param("id", String.valueOf(CENTER_ID)).param("clientId",
                                                                "invalid"))
                                .andExpect(status().isBadRequest());

                then(centerService).should(never()).read(any(CenterRead.class));
        }
}
