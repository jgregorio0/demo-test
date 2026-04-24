package dev.jgregorio.demo.data.center;

import dev.jgregorio.demo.data.infrastructure.api.center.CenterResponse;
import dev.jgregorio.demo.data.infrastructure.api.center.create.CenterCreationRequest;
import dev.jgregorio.demo.data.infrastructure.api.center.update.CenterUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CenterIT {

    @Autowired
    private TestRestTemplate restTemplate;

    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql"})
    void create_shouldInsertCenter() {
        // Given
        CenterCreationRequest request = CenterCreationRequest.builder()
                .clientId(1L).name("Center 1").address("Address 1")
                .postalCode("Postal Code 1").locationId(1L).build();
        // When
        ResponseEntity<CenterResponse> response = restTemplate.postForEntity("/centers", request, CenterResponse.class);
        // Then
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody().id());
        assertEquals(request.name(), response.getBody().name());
        assertEquals(request.address(), response.getBody().address());
        assertEquals(request.postalCode(), response.getBody().postalCode());
        assertEquals(request.locationId(), response.getBody().location().id());
    }

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql"})
    void create_shouldReturnLocationHeader() {
        // Given
        CenterCreationRequest request = CenterCreationRequest.builder()
                .clientId(1L).name("Center 1").address("Address 1")
                .postalCode("Postal Code 1").locationId(1L).build();
        // When
        ResponseEntity<CenterResponse> response = restTemplate.postForEntity("/centers", request, CenterResponse.class);
        // Then
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getHeaders().getLocation());
        assertTrue(response.getHeaders().getLocation().toString().contains("/clients/1/centers/"));
    }

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql"})
    void create_withMissingRequiredFields_shouldReturn400() {
        // Given — name is blank
        CenterCreationRequest request = CenterCreationRequest.builder()
                .clientId(1L).name("").address("Address 1")
                .postalCode("Postal Code 1").locationId(1L).build();
        // When
        ResponseEntity<Void> response = restTemplate.postForEntity("/centers", request, Void.class);
        // Then
        assertEquals(400, response.getStatusCode().value());
    }

    // -------------------------------------------------------------------------
    // READ
    // -------------------------------------------------------------------------

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql", "/center/center_1.sql"})
    void read_shouldReturnCenter() {
        // When
        ResponseEntity<CenterResponse> response = restTemplate.getForEntity(
                "/centers?id=1&clientId=1", CenterResponse.class);
        // Then
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().id());
        assertEquals(1L, response.getBody().clientId());
        assertEquals("Center 1", response.getBody().name());
        assertEquals("Address 1", response.getBody().address());
        assertEquals("Postal Code 1", response.getBody().postalCode());
        assertEquals(1L, response.getBody().location().id());
    }

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql"})
    void read_withNonExistentId_shouldReturn404() {
        // When
        ResponseEntity<Void> response = restTemplate.getForEntity(
                "/centers?id=999&clientId=1", Void.class);
        // Then
        assertEquals(404, response.getStatusCode().value());
    }

    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql", "/center/center_1.sql"})
    void update_shouldModifyCenter() {
        // Given
        CenterUpdateRequest request = CenterUpdateRequest.builder()
                .id(1L).clientId(1L).name("Updated Center").address("Updated Address")
                .postalCode("Updated Postal").locationId(1L).build();
        // When
        ResponseEntity<CenterResponse> response = restTemplate.exchange(
                "/centers", HttpMethod.PUT, new HttpEntity<>(request), CenterResponse.class);
        // Then
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().id());
        assertEquals("Updated Center", response.getBody().name());
        assertEquals("Updated Address", response.getBody().address());
        assertEquals("Updated Postal", response.getBody().postalCode());
    }

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql"})
    void update_withNonExistentId_shouldReturn404() {
        // Given
        CenterUpdateRequest request = CenterUpdateRequest.builder()
                .id(999L).clientId(1L).name("Updated Center").address("Updated Address")
                .postalCode("Updated Postal").locationId(1L).build();
        // When
        ResponseEntity<Void> response = restTemplate.exchange(
                "/centers", HttpMethod.PUT, new HttpEntity<>(request), Void.class);
        // Then
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql", "/center/center_1.sql"})
    void update_withMissingRequiredFields_shouldReturn400() {
        // Given — name is blank
        CenterUpdateRequest request = CenterUpdateRequest.builder()
                .id(1L).clientId(1L).name("").address("Updated Address")
                .postalCode("Updated Postal").locationId(1L).build();
        // When
        ResponseEntity<Void> response = restTemplate.exchange(
                "/centers", HttpMethod.PUT, new HttpEntity<>(request), Void.class);
        // Then
        assertEquals(400, response.getStatusCode().value());
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql", "/center/center_1.sql"})
    void delete_shouldRemoveCenter() {
        // When
        ResponseEntity<Void> response = restTemplate.exchange(
                "/centers?id=1&clientId=1", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        // Then
        assertEquals(204, response.getStatusCode().value());

        // Verify it no longer exists
        ResponseEntity<Void> readResponse = restTemplate.getForEntity(
                "/centers?id=1&clientId=1", Void.class);
        assertEquals(404, readResponse.getStatusCode().value());
    }

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql"})
    void delete_withNonExistentId_shouldReturn204() {
        // Note: current implementation delegates to JpaRepository.deleteById which does not
        // throw when the entity does not exist (Spring Boot 3.x behavior), so 204 is returned.
        // See GenericJpaPersistenceAdapter analysis — this is a known improvement point.
        ResponseEntity<Void> response = restTemplate.exchange(
                "/centers?id=999&clientId=1", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        // Then
        assertEquals(204, response.getStatusCode().value());
    }

    // -------------------------------------------------------------------------
    // SEARCH
    // -------------------------------------------------------------------------

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql", "/center/center_1.sql"})
    void search_byName_shouldReturnMatchingCenters() {
        // When
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/centers/search?name=Center 1",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<Map<String, Object>>() {});
        // Then
        assertEquals(200, response.getStatusCode().value());
        List<?> content = (List<?>) response.getBody().get("content");
        assertFalse(content.isEmpty());
        Map<?, ?> first = (Map<?, ?>) content.get(0);
        assertEquals("Center 1", first.get("name"));
    }

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql", "/center/center_1.sql"})
    void search_withNoMatch_shouldReturnEmptyPage() {
        // When
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/centers/search?name=NonExistent",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<Map<String, Object>>() {});
        // Then
        assertEquals(200, response.getStatusCode().value());
        List<?> content = (List<?>) response.getBody().get("content");
        assertTrue(content.isEmpty());
    }
}
