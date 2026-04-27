package dev.jgregorio.demo.data.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import dev.jgregorio.demo.data.application.out.user.UserPersistencePort;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserPersistencePort userPersistence;
    @InjectMocks
    private UserService userService;

    @Test
    void getByUsername_shouldReturnUser_whenUserExists() {
        // Given
        String username = "testuser";
        User user = User.builder().username(username).build();
        given(userPersistence.getByUsername(username)).willReturn(Optional.of(user));
        // When
        Optional<User> result = userService.getByUsername(username);
        // Then
        assertThat(result).isPresent().contains(user);
        then(userPersistence).should().getByUsername(username);
    }

    @Test
    void getByUsername_shouldReturnEmptyOptional_whenUserDoesNotExist() {
        // Given
        String username = "nonexistent";
        given(userPersistence.getByUsername(username)).willReturn(Optional.empty());
        // When
        Optional<User> result = userService.getByUsername(username);
        // Then
        assertThat(result).isNotPresent();
        then(userPersistence).should().getByUsername(username);
    }

    @Test
    void search_shouldReturnListOfUsers_whenUsersMatchCriteria() {
        // Given
        UserSearch criteria = UserSearch.builder().build();
        Sort sort = Sort.by("username");
        List<User> expectedUsers = List.of(User.builder().username("user1").build(),
                User.builder().username("user2").build());
        given(userPersistence.search(criteria, sort)).willReturn(expectedUsers);
        // When
        List<User> result = userService.search(criteria, sort);
        // Then
        assertThat(result).isEqualTo(expectedUsers);
        then(userPersistence).should().search(criteria, sort);
    }

    @Test
    void search_shouldReturnEmptyList_whenNoUsersMatchCriteria() {
        // Given
        UserSearch criteria = UserSearch.builder().build();
        Sort sort = Sort.by("username");
        given(userPersistence.search(criteria, sort)).willReturn(List.of());
        // When
        List<User> result = userService.search(criteria, sort);
        // Then
        assertThat(result).isEmpty();
        then(userPersistence).should().search(criteria, sort);
    }
}
