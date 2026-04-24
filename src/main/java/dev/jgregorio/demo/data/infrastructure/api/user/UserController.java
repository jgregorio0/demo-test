package dev.jgregorio.demo.data.infrastructure.api.user;

import dev.jgregorio.demo.data.domain.user.User;
import dev.jgregorio.demo.data.domain.user.UserSearch;
import dev.jgregorio.demo.data.domain.user.UserService;
import dev.jgregorio.demo.data.infrastructure.api.user.search.UserSearchApiMapper;
import dev.jgregorio.demo.data.infrastructure.api.user.search.UserSearchRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserSearchApiMapper userSearchApiMapper;

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> search(
            @Valid final UserSearchRequest searchRequest, final Sort sort) {
        UserSearch criteria = userSearchApiMapper.fromRequest(searchRequest);
        List<User> users = userService.search(criteria, sort);
        List<UserResponse> responseUsers = users.stream().map(userSearchApiMapper::toResponse).toList();
        return ResponseEntity.ok(responseUsers);
    }
}
