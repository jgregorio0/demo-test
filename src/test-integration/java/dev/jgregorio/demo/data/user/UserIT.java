package dev.jgregorio.demo.data.user;

import dev.jgregorio.demo.data.IntegrationTestBase;
import dev.jgregorio.demo.data.infrastructure.api.user.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserIT extends IntegrationTestBase {

    @Test
    @Sql("/user/user_1.sql")
    void search_shouldReturnMatchingUsers_whenSearchingByFirstName() {
        // When
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                "/users/search?fullName=John",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<UserResponse>>() {});

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody())
                .isNotNull()
                .isNotEmpty();
        assertThat(response.getBody().get(0).name()).isEqualTo("John");
        assertThat(response.getBody().get(0).surname()).isEqualTo("Doe");
    }

    @Test
    @Sql("/user/user_1.sql")
    void search_shouldReturnMatchingUsers_whenSearchingBySurname() {
        // When
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                "/users/search?fullName=Doe",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<UserResponse>>() {});

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody())
                .isNotNull()
                .isNotEmpty();
        assertThat(response.getBody().get(0).surname()).isEqualTo("Doe");
    }

    @Test
    @Sql("/user/user_1.sql")
    void search_shouldReturnMatchingUsers_whenSearchingByFullName() {
        // When
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                "/users/search?fullName=John Doe",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<UserResponse>>() {});

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody())
                .isNotNull()
                .isNotEmpty();
        assertThat(response.getBody().get(0).name()).isEqualTo("John");
        assertThat(response.getBody().get(0).surname()).isEqualTo("Doe");
    }

    @Test
    @Sql("/user/user_1.sql")
    void search_shouldReturnEmptyList_whenNoMatch() {
        // When
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                "/users/search?fullName=NonExistent",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<UserResponse>>() {});

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull().isEmpty();
    }

    @Test
    void search_shouldReturn400_whenFullNameIsBlank() {
        // When
        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/search?fullName=",
                HttpMethod.GET, null,
                Void.class);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}
