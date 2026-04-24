package dev.jgregorio.demo.data.infrastructure.persistence.center;

import dev.jgregorio.demo.data.application.out.center.CenterPersistencePort;
import dev.jgregorio.demo.data.domain.center.Center;
import dev.jgregorio.demo.data.domain.center.CenterSearch;
import dev.jgregorio.demo.data.infrastructure.persistence.GenericJpaPersistenceAdapter;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CenterJpaPersistenceAdapter
        extends GenericJpaPersistenceAdapter<Center, CenterEntity, CenterEntityId>
        implements CenterPersistencePort {

    public CenterJpaPersistenceAdapter(
            CenterPersistenceMapper mapper, CenterJpaPersistenceRepository repository) {
        super(mapper, repository, CenterEntity::new, centerEntityIdFunction());
    }

    private static Function<Center, CenterEntityId> centerEntityIdFunction() {
        return (domain) -> CenterEntityId.from(domain.id(), domain.clientId());
    }

    public CenterJpaPersistenceRepository getRepository() {
        return (CenterJpaPersistenceRepository) jpaRepository;
    }

    @Override
    public Optional<Center> read(Long id, Long clientId) {
        Optional<CenterEntity> entity = getRepository().findById(CenterEntityId.from(id, clientId));
        return entity.map(mapper::toDomain);
    }

    @Override
    public void delete(Long id, Long clientId) {
        getRepository().deleteById(CenterEntityId.from(id, clientId));
    }

    @Override
    public Page<Center> search(CenterSearch criteria, Pageable pageable) {
        Specification<CenterEntity> specification = CenterSpecification.search(criteria);
        return getRepository().findAll(specification, pageable).map(mapper::toDomain);
    }
}
