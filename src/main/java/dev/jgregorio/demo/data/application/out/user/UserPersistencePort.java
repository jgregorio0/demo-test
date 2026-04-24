package dev.jgregorio.demo.data.application.out.user;

import dev.jgregorio.demo.data.domain.user.User;
import dev.jgregorio.demo.data.domain.user.UserSearch;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

public interface UserPersistencePort {

    Optional<User> getByUsername(String username);

    List<User> search(UserSearch criteria, Sort sort);
}
