package dev.jgregorio.demo.data.domain.center;

import dev.jgregorio.demo.data.application.in.center.CenterUseCase;
import dev.jgregorio.demo.data.application.out.center.CenterPersistencePort;
import dev.jgregorio.demo.data.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CenterService implements CenterUseCase {

    private final CenterPersistencePort persistence;

    @Transactional
    @Override
    public Center create(CenterCreation creation) {
        Center center = creation.toCenter();
        return persistence.create(center);
    }

    @Transactional(readOnly = true)
    @Override
    public Center read(CenterRead read) {
        return persistence
                .read(read.id(), read.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Center not found"));
    }

    @Transactional
    @Override
    public Center update(CenterUpdate update) {
        CenterRead toRead = CenterRead.builder().id(update.id()).clientId(update.clientId()).build();
        Center center = read(toRead);
        center = update.toCenter(center);
        return persistence.update(center);
    }

    @Transactional
    public void delete(CenterDelete delete) {
        persistence.delete(delete.id(), delete.clientId());
    }

    @Transactional(readOnly = true)
    public Page<Center> search(CenterSearch criteria, Pageable pageable) {
        return persistence.search(criteria, pageable);
    }
}
