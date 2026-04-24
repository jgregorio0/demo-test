package dev.jgregorio.demo.data.domain.user;

import dev.jgregorio.demo.data.application.out.user.UserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserPersistencePort userPersistence;

    // Starts a new transaction (REQUIRES_NEW) to break the circular dependency with JPA Auditing.
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Optional<User> getByUsername(String username) {
        return userPersistence.getByUsername(username);
    }

    public List<User> search(UserSearch criteria, Sort sort) {
        return userPersistence.search(criteria, sort);
    }
}
