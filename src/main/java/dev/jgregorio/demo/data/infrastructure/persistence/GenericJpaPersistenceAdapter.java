package dev.jgregorio.demo.data.infrastructure.persistence;

import dev.jgregorio.demo.data.application.GenericPersistence;
import dev.jgregorio.demo.data.domain.exception.ResourceNotFoundException;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.data.jpa.repository.JpaRepository;

@lombok.RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class GenericJpaPersistenceAdapter<D, E, I> implements GenericPersistence<D, I> {

    protected final GenericPersistenceMapper<D, E> mapper;
    protected final JpaRepository<E, I> jpaRepository;
    protected final Supplier<E> entityFactory;
    protected final Function<D, I> idFunction;

    @Override
    public D create(D domain) {
        E entity = entityFactory.get();
        mapper.update(entity, domain);
        E saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<D> read(I id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public D update(D domain) {
        I id = idFunction.apply(domain);
        E entity =
                jpaRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                ResourceNotFoundException.formatMessage(
                                                        entityFactory.get().getClass(), id)));
        mapper.update(entity, domain);
        E saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void delete(I id) {
        jpaRepository.deleteById(id);
    }
}
