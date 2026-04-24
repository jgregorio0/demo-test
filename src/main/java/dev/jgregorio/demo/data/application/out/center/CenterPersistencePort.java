package dev.jgregorio.demo.data.application.out.center;

import dev.jgregorio.demo.data.domain.center.Center;
import dev.jgregorio.demo.data.domain.center.CenterSearch;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CenterPersistencePort {
    Center create(Center center);

    Optional<Center> read(Long id, Long clientId);

    Center update(Center center);

    void delete(Long id, Long clientId);

    Page<Center> search(CenterSearch criteria, Pageable pageable);
}
