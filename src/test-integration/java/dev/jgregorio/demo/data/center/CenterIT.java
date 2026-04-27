package dev.jgregorio.demo.data.center;

import dev.jgregorio.demo.data.infrastructure.api.center.CenterResponse;
import dev.jgregorio.demo.data.infrastructure.api.center.create.CenterCreationRequest;
import dev.jgregorio.demo.data.infrastructure.api.center.update.CenterUpdateRequest;
import dev.jgregorio.demo.data.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CenterIT extends IntegrationTestBase {

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql"})
    void create_shouldInsertCenter() {
        // Given
        CenterCreationRequest request = CenterCreationRequest.builder()
                .clientId(1L).name("Center 1").address("Address 1")
                .postalCode("Postal Code 1").locationId(1L).build();
        // When
        ResponseEntity<CenterResponse> response = restTemplate.postForEntity("/centers", request,
                CenterResponse.class);
        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        CenterResponse body = response.getBody();
        assertThat(body.id()).isNotNull();
        assertThat(body.name()).isEqualTo(request.name());
        assertThat(body.address()).isEqualTo(request.address());
        assertThat(body.postalCode()).isEqualTo(request.postalCode());
        assertThat(body.location().id()).isEqualTo(request.locationId());
        assertThat(response.getHeaders().getLocation()).isEqualTo(body.locationUri());
    }

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql"})
    void create_shouldReturnLocationHeader() {
        // Given
        CenterCreationRequest request = CenterCreationRequest.builder()
                .clientId(1L).name("Center 1").address("Address 1")
                .postalCode("Postal Code 1").locationId(1L).build();
        // When
        ResponseEntity<CenterResponse> response = restTemplate.postForEntity("/centers", request,
                CenterResponse.class);
        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getHeaders().getLocation().toString()).contains("/clients/1/centers/");
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
        assertThat(response.getStatusCode().value()).isEqualTo(400);
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
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().id()).isEqualTo(1L);
        assertThat(response.getBody().clientId()).isEqualTo(1L);
        assertThat(response.getBody().name()).isEqualTo("Center 1");
        assertThat(response.getBody().address()).isEqualTo("Address 1");
        assertThat(response.getBody().postalCode()).isEqualTo("Postal Code 1");
        assertThat(response.getBody().location().id()).isEqualTo(1L);
    }

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql"})
    void read_withNonExistentId_shouldReturn404() {
        // When
        ResponseEntity<Void> response = restTemplate.getForEntity(
                "/centers?id=999&clientId=1", Void.class);
        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(404);
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
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().id()).isEqualTo(1L);
        assertThat(response.getBody().name()).isEqualTo("Updated Center");
        assertThat(response.getBody().address()).isEqualTo("Updated Address");
        assertThat(response.getBody().postalCode()).isEqualTo("Updated Postal");
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
        assertThat(response.getStatusCode().value()).isEqualTo(404);
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
        assertThat(response.getStatusCode().value()).isEqualTo(400);
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
        assertThat(response.getStatusCode().value()).isEqualTo(204);

        // Verify it no longer exists
        ResponseEntity<Void> readResponse = restTemplate.getForEntity(
                "/centers?id=1&clientId=1", Void.class);
        assertThat(readResponse.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql"})
    void delete_withNonExistentId_shouldReturn204() {
        // Note: current implementation delegates to JpaRepository.deleteById which does
        // not
        // throw when the entity does not exist (Spring Boot 3.x behavior), so 204 is
        // returned.
        // See GenericJpaPersistenceAdapter analysis — this is a known improvement
        // point.
        ResponseEntity<Void> response = restTemplate.exchange(
                "/centers?id=999&clientId=1", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(204);
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
                new ParameterizedTypeReference<Map<String, Object>>() {
                });
        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        List<?> content = (List<?>) response.getBody().get("content");
        assertThat(content).isNotEmpty();
        Map<?, ?> first = (Map<?, ?>) content.get(0);
        assertThat(first.get("name")).isEqualTo("Center 1");
    }

    @Test
    @Sql({"/location/location_1.sql", "/client/client_1.sql", "/center/center_1.sql"})
    void search_withNoMatch_shouldReturnEmptyPage() {
        // When
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/centers/search?name=NonExistent",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });
        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        List<?> content = (List<?>) response.getBody().get("content");
        assertThat(content).isEmpty();
    }
}
