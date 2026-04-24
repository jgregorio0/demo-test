package dev.jgregorio.demo.data.domain.center;

import dev.jgregorio.demo.data.application.out.center.CenterPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.createCenter;
import static dev.jgregorio.demo.data.domain.center.CenterServiceTestDataFactory.createCenterSearch;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchCenterServiceTest {

    @Mock
    private CenterPersistencePort persistence;
    @InjectMocks
    private CenterService service;

    private CenterSearch centerSearch;
    private Center center;

    @BeforeEach
    void setUp() {
        centerSearch = createCenterSearch();
        center = createCenter();
    }

    @Test
    @DisplayName("search should call persistence with criteria and pageable")
    void search_shouldCallPersistenceWithCriteriaAndPageable() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Center> centers = List.of(center);
        Page<Center> expectedPage = new PageImpl<>(centers, pageable, centers.size());

        when(persistence.search(centerSearch, pageable)).thenReturn(expectedPage);

        // When
        Page<Center> result = service.search(centerSearch, pageable);

        // Then
        then(persistence).should().search(centerSearch, pageable);
        assertThat(result).isNotNull();
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("search should return empty page when no results found")
    void search_shouldReturnEmptyPage_whenNoResultsFound() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Center> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(persistence.search(centerSearch, pageable)).thenReturn(emptyPage);

        // When
        Page<Center> result = service.search(centerSearch, pageable);

        // Then
        then(persistence).should().search(centerSearch, pageable);
        assertThat(result).isNotNull();
        assertEquals(0, result.getTotalElements());
    }
}
