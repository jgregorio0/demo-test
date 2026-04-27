package dev.jgregorio.demo.data.infrastructure.api.center;

import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.CLIENT_ID;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.LOCATION_ID;
import static dev.jgregorio.demo.data.infrastructure.api.center.CenterControllerTestDataFactory.createCenter;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.jgregorio.demo.data.application.in.center.CenterUseCase;
import dev.jgregorio.demo.data.domain.center.Center;
import dev.jgregorio.demo.data.domain.center.CenterSearch;
import dev.jgregorio.demo.data.domain.location.Location;
import dev.jgregorio.demo.data.infrastructure.api.center.create.CenterCreationApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.delete.CenterDeleteApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.read.CenterReadApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.search.CenterSearchApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.center.search.CenterSearchRequest;
import dev.jgregorio.demo.data.infrastructure.api.center.search.CenterSearchResponse;
import dev.jgregorio.demo.data.infrastructure.api.center.update.CenterUpdateApiMapper;

@WebMvcTest(CenterController.class)
class SearchCenterControllerTest {

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
        @DisplayName("should search centers and return results when criteria matches")
        void search_shouldFindCenter_whenRequestIsValid() throws Exception {
                // Given
                CenterSearch centerSearch = CenterSearch.builder().name("Test").build();
                Center center = createCenter();
                Pageable pageable = PageRequest.of(0, 10);
                Page<Center> foundCenters = new PageImpl<>(List.of(center), pageable, 1);
                CenterSearchResponse searchResponse = CenterSearchResponse.builder()
                                .id(center.id())
                                .name(center.name())
                                .address(center.address())
                                .postalCode(center.postalCode())
                                .location(center.location())
                                .build();

                given(searchMapper.fromRequest(any(CenterSearchRequest.class))).willReturn(centerSearch);
                given(centerService.search(centerSearch, pageable)).willReturn(foundCenters);
                given(searchMapper.toResponse(center)).willReturn(searchResponse);

                // When & Then
                mockMvc
                                .perform(get("/centers/search").param("name", "Test"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id", is(searchResponse.id().intValue())))
                                .andExpect(jsonPath("$.content[0].name", is(searchResponse.name())))
                                .andExpect(jsonPath("$.content[0].address", is(searchResponse.address())))
                                .andExpect(jsonPath("$.content[0].postalCode", is(searchResponse.postalCode())))
                                .andExpect(
                                                jsonPath("$.content[0].location.id",
                                                                is(searchResponse.location().id().intValue())));

                then(searchMapper).should().fromRequest(any(CenterSearchRequest.class));
                then(centerService).should().search(centerSearch, pageable);
                then(searchMapper).should().toResponse(center);
        }

        @Test
        @DisplayName("should return empty page when no centers match search criteria")
        void search_shouldReturnEmptyPage_whenNoCentersFound() throws Exception {
                // Given
                CenterSearch centerSearch = CenterSearch.builder().name("NonExistent").build();
                Pageable pageable = PageRequest.of(0, 10);
                Page<Center> emptyPage = new PageImpl<>(List.of(), pageable, 0);

                given(searchMapper.fromRequest(any(CenterSearchRequest.class))).willReturn(centerSearch);
                given(centerService.search(centerSearch, pageable)).willReturn(emptyPage);

                // When & Then
                mockMvc
                                .perform(get("/centers/search").param("name", "NonExistent"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isEmpty());

                then(searchMapper).should().fromRequest(any(CenterSearchRequest.class));
                then(centerService).should().search(centerSearch, pageable);
                then(searchMapper).should(never()).toResponse(any(Center.class));
        }

        @Test
        @DisplayName("should search with all parameters when all criteria provided")
        void search_shouldSearchWithAllParameters_whenAllCriteriaProvided() throws Exception {
                // Given
                CenterSearch centerSearch = CenterSearch.builder().name("Test").address("123 Main").postalCode("12345")
                                .build();
                Center center = createCenter();
                Pageable pageable = PageRequest.of(0, 10);
                Page<Center> foundCenters = new PageImpl<>(List.of(center), pageable, 1);
                CenterSearchResponse searchResponse = CenterSearchResponse.builder()
                                .id(center.id())
                                .name(center.name())
                                .address(center.address())
                                .postalCode(center.postalCode())
                                .location(center.location())
                                .build();

                given(searchMapper.fromRequest(any(CenterSearchRequest.class))).willReturn(centerSearch);
                given(centerService.search(centerSearch, pageable)).willReturn(foundCenters);
                given(searchMapper.toResponse(center)).willReturn(searchResponse);

                // When & Then
                mockMvc
                                .perform(
                                                get("/centers/search")
                                                                .param("name", "Test")
                                                                .param("address", "123 Main")
                                                                .param("postalCode", "12345"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id", is(searchResponse.id().intValue())));

                then(searchMapper).should().fromRequest(any(CenterSearchRequest.class));
                then(centerService).should().search(centerSearch, pageable);
                then(searchMapper).should().toResponse(center);
        }

        @Test
        @DisplayName("should search with no parameters when no criteria provided")
        void search_shouldSearchWithNoParameters_whenNoCriteriaProvided() throws Exception {
                // Given
                CenterSearch centerSearch = CenterSearch.builder().build();
                Center center = createCenter();
                Pageable pageable = PageRequest.of(0, 10);
                Page<Center> foundCenters = new PageImpl<>(List.of(center), pageable, 1);
                CenterSearchResponse searchResponse = CenterSearchResponse.builder()
                                .id(center.id())
                                .name(center.name())
                                .address(center.address())
                                .postalCode(center.postalCode())
                                .location(center.location())
                                .build();

                given(searchMapper.fromRequest(any(CenterSearchRequest.class))).willReturn(centerSearch);
                given(centerService.search(centerSearch, pageable)).willReturn(foundCenters);
                given(searchMapper.toResponse(center)).willReturn(searchResponse);

                // When & Then
                mockMvc
                                .perform(get("/centers/search"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id", is(searchResponse.id().intValue())));

                then(searchMapper).should().fromRequest(any(CenterSearchRequest.class));
                then(centerService).should().search(centerSearch, pageable);
        }

        @Test
        @DisplayName("should return multiple centers when multiple results found")
        void search_shouldReturnMultipleCenters_whenMultipleResultsFound() throws Exception {
                // Given
                CenterSearch centerSearch = CenterSearch.builder().name("Center").build();
                Center center1 = createCenter();
                Center center2 = Center.builder()
                                .id(2L)
                                .clientId(CLIENT_ID)
                                .name("Center B")
                                .address("456 Second St")
                                .postalCode("54321")
                                .location(Location.from(LOCATION_ID))
                                .build();

                Pageable pageable = PageRequest.of(0, 10);
                Page<Center> foundCenters = new PageImpl<>(List.of(center1, center2), pageable, 2);

                CenterSearchResponse response1 = CenterSearchResponse.builder()
                                .id(center1.id())
                                .name(center1.name())
                                .address(center1.address())
                                .postalCode(center1.postalCode())
                                .location(center1.location())
                                .build();

                CenterSearchResponse response2 = CenterSearchResponse.builder()
                                .id(center2.id())
                                .name(center2.name())
                                .address(center2.address())
                                .postalCode(center2.postalCode())
                                .location(center2.location())
                                .build();

                given(searchMapper.fromRequest(any(CenterSearchRequest.class))).willReturn(centerSearch);
                given(centerService.search(centerSearch, pageable)).willReturn(foundCenters);
                given(searchMapper.toResponse(center1)).willReturn(response1);
                given(searchMapper.toResponse(center2)).willReturn(response2);

                // When & Then
                mockMvc
                                .perform(get("/centers/search").param("name", "Center"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id", is(response1.id().intValue())))
                                .andExpect(jsonPath("$.content[0].name", is(response1.name())))
                                .andExpect(jsonPath("$.content[1].id", is(response2.id().intValue())))
                                .andExpect(jsonPath("$.content[1].name", is(response2.name())));

                then(searchMapper).should().fromRequest(any(CenterSearchRequest.class));
                then(centerService).should().search(centerSearch, pageable);
                then(searchMapper).should().toResponse(center1);
                then(searchMapper).should().toResponse(center2);
        }

        @Test
        @DisplayName("should handle pagination parameters correctly")
        void search_shouldHandlePagination_whenPageParametersProvided() throws Exception {
                // Given
                CenterSearch centerSearch = CenterSearch.builder().build();
                Center center = createCenter();
                Pageable pageable = PageRequest.of(1, 5);
                Page<Center> foundCenters = new PageImpl<>(List.of(center), pageable, 10);
                CenterSearchResponse searchResponse = CenterSearchResponse.builder()
                                .id(center.id())
                                .name(center.name())
                                .address(center.address())
                                .postalCode(center.postalCode())
                                .location(center.location())
                                .build();

                given(searchMapper.fromRequest(any(CenterSearchRequest.class))).willReturn(centerSearch);
                given(centerService.search(eq(centerSearch), any(Pageable.class))).willReturn(foundCenters);
                given(searchMapper.toResponse(center)).willReturn(searchResponse);

                // When & Then
                mockMvc
                                .perform(get("/centers/search").param("page", "1").param("size", "5"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id", is(searchResponse.id().intValue())));

                then(searchMapper).should().fromRequest(any(CenterSearchRequest.class));
                then(centerService).should().search(eq(centerSearch), any(Pageable.class));
        }

        @Test
        @DisplayName("should handle empty string parameters as null")
        void search_shouldHandleEmptyStrings_whenEmptyParametersProvided() throws Exception {
                // Given
                CenterSearch centerSearch = CenterSearch.builder().build();
                Center center = createCenter();
                Pageable pageable = PageRequest.of(0, 10);
                Page<Center> foundCenters = new PageImpl<>(List.of(center), pageable, 1);
                CenterSearchResponse searchResponse = CenterSearchResponse.builder()
                                .id(center.id())
                                .name(center.name())
                                .address(center.address())
                                .postalCode(center.postalCode())
                                .location(center.location())
                                .build();

                given(searchMapper.fromRequest(any(CenterSearchRequest.class))).willReturn(centerSearch);
                given(centerService.search(centerSearch, pageable)).willReturn(foundCenters);
                given(searchMapper.toResponse(center)).willReturn(searchResponse);

                // When & Then
                mockMvc
                                .perform(
                                                get("/centers/search").param("name", "").param("address", "")
                                                                .param("postalCode", ""))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id", is(searchResponse.id().intValue())));

                then(searchMapper).should().fromRequest(any(CenterSearchRequest.class));
                then(centerService).should().search(centerSearch, pageable);
        }

        @Test
        @DisplayName("should search by name only when only name provided")
        void search_shouldSearchByNameOnly_whenOnlyNameProvided() throws Exception {
                // Given
                CenterSearch centerSearch = CenterSearch.builder().name("Test").build();
                Center center = createCenter();
                Pageable pageable = PageRequest.of(0, 10);
                Page<Center> foundCenters = new PageImpl<>(List.of(center), pageable, 1);
                CenterSearchResponse searchResponse = CenterSearchResponse.builder()
                                .id(center.id())
                                .name(center.name())
                                .address(center.address())
                                .postalCode(center.postalCode())
                                .location(center.location())
                                .build();

                given(searchMapper.fromRequest(any(CenterSearchRequest.class))).willReturn(centerSearch);
                given(centerService.search(centerSearch, pageable)).willReturn(foundCenters);
                given(searchMapper.toResponse(center)).willReturn(searchResponse);

                // When & Then
                mockMvc
                                .perform(get("/centers/search").param("name", "Test"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].name", is(searchResponse.name())));

                then(searchMapper).should().fromRequest(any(CenterSearchRequest.class));
                then(centerService).should().search(centerSearch, pageable);
        }
}
