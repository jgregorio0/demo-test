package dev.jgregorio.demo.data.infrastructure.persistence.center;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

import dev.jgregorio.demo.data.domain.center.CenterSearch;

@DataJpaTest
@Sql("insert_centers_for_spec.sql")
class CenterSpecificationTest {

    @Autowired
    private CenterJpaPersistenceRepository repository;

    @Test
    void search_shouldReturnMatchingCenters_whenNameMatches() {
        // Given
        CenterSearch criteria = CenterSearch.builder().name("spring boot").build();
        Specification<CenterEntity> spec = CenterSpecification.search(criteria);

        // When
        List<CenterEntity> result = repository.findAll(spec);

        // Then
        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(c -> c.getId().getId())
                .isEqualTo(1L);
    }

    @Test
    void search_shouldReturnMatchingCenters_whenAddressMatches() {
        // Given
        CenterSearch criteria = CenterSearch.builder().address("main street").build();
        Specification<CenterEntity> spec = CenterSpecification.search(criteria);

        // When
        List<CenterEntity> result = repository.findAll(spec);

        // Then
        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(c -> c.getId().getId())
                .isEqualTo(1L);
    }

    @Test
    void search_shouldReturnMatchingCenters_whenPostalCodeMatches() {
        // Given
        CenterSearch criteria = CenterSearch.builder().postalCode("28001").build();
        Specification<CenterEntity> spec = CenterSpecification.search(criteria);

        // When
        List<CenterEntity> result = repository.findAll(spec);

        // Then
        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(c -> c.getId().getId())
                .isEqualTo(1L);
    }

    @Test
    void search_shouldReturnAllCenters_whenNoCriteriaProvided() {
        // Given
        CenterSearch criteria = CenterSearch.builder().build();
        Specification<CenterEntity> spec = CenterSpecification.search(criteria);

        // When
        List<CenterEntity> result = repository.findAll(spec);

        // Then
        assertThat(result).hasSize(2);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void search_shouldReturnAllCenters_whenNameIsBlank(String blankName) {
        // Given
        CenterSearch criteria = CenterSearch.builder().name(blankName).build();
        Specification<CenterEntity> spec = CenterSpecification.search(criteria);

        // When
        List<CenterEntity> result = repository.findAll(spec);

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    void search_shouldReturnMatchingCenters_whenPartialNameMatches() {
        // Given
        CenterSearch criteria = CenterSearch.builder().name("hub").build();
        Specification<CenterEntity> spec = CenterSpecification.search(criteria);

        // When
        List<CenterEntity> result = repository.findAll(spec);

        // Then
        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(c -> c.getId().getId())
                .isEqualTo(2L);
    }

    @Test
    void search_shouldReturnEmpty_whenNoMatch() {
        // Given
        CenterSearch criteria = CenterSearch.builder().name("NonExistent").build();
        Specification<CenterEntity> spec = CenterSpecification.search(criteria);

        // When
        List<CenterEntity> result = repository.findAll(spec);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void search_shouldReturnMatchingCenters_whenMultipleCriteriaMatch() {
        // Given
        CenterSearch criteria = CenterSearch.builder()
                .name("Spring Boot Center")
                .address("Main Street")
                .postalCode("28001")
                .build();
        Specification<CenterEntity> spec = CenterSpecification.search(criteria);

        // When
        List<CenterEntity> result = repository.findAll(spec);

        // Then
        assertThat(result).hasSize(1);
    }
}
