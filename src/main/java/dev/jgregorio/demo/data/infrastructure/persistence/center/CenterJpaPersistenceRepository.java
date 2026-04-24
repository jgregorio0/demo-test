package dev.jgregorio.demo.data.infrastructure.persistence.center;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CenterJpaPersistenceRepository
        extends JpaRepository<CenterEntity, CenterEntityId>, JpaSpecificationExecutor<CenterEntity> {
}
