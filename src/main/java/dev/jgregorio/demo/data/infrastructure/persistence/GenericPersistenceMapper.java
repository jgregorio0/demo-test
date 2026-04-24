package dev.jgregorio.demo.data.infrastructure.persistence;

import java.util.List;

import org.mapstruct.MappingTarget;

public interface GenericPersistenceMapper<D, E> {

    D toDomain(E entity);

    E toEntity(D domain);

    List<D> toDomainList(List<E> entities);

    List<E> toEntityList(List<D> domains);

    default void update(@MappingTarget E entity, D domain) {
        throw new UnsupportedOperationException(
                "Update method not implemented for " + entity.getClass().getName());
    }
}
