package dev.jgregorio.demo.data.infrastructure.persistence.center.search;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.*;

import dev.jgregorio.demo.data.domain.center.Center;
import dev.jgregorio.demo.data.domain.center.CenterSearch;
import dev.jgregorio.demo.data.infrastructure.persistence.center.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class CenterSearchJpaPersistenceTest {

    @Mock
    private CenterPersistenceMapper mapper;
    @Mock
    private CenterJpaPersistenceRepository jpaRepository;
    @InjectMocks
    private CenterJpaPersistenceAdapter adapter;

    @Test
    void search_shouldReturnPageOfCenters_whenCriteriaIsProvided() {
        // Given
        CenterSearch criteria = CenterSearch.builder().build();
        Pageable pageable = Pageable.unpaged();
        CenterEntity centerEntity = new CenterEntity();
        Page<CenterEntity> entitiesPage = new PageImpl<>(Collections.singletonList(centerEntity));
        Center center = Center.builder().build();
        try (MockedStatic<CenterSpecification> mockedSpec = mockStatic(CenterSpecification.class)) {
            mockedSpec
                    .when(() -> CenterSpecification.search(criteria))
                    .thenReturn(Specification.unrestricted());
            when(jpaRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(entitiesPage);
            when(mapper.toDomain(centerEntity)).thenReturn(center);
            // When
            adapter.search(criteria, pageable);
            // Then
            then(jpaRepository).should().findAll(any(Specification.class), eq(pageable));
            then(mapper).should().toDomain(centerEntity);
        }
    }
}
