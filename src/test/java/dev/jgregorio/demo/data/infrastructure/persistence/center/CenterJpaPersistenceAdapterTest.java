package dev.jgregorio.demo.data.infrastructure.persistence.center;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;

import java.util.List;
import java.util.Optional;

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
import org.springframework.data.jpa.domain.Specification;

import dev.jgregorio.demo.data.domain.center.Center;
import dev.jgregorio.demo.data.domain.center.CenterSearch;
import dev.jgregorio.demo.data.domain.exception.ResourceNotFoundException;
import dev.jgregorio.demo.data.domain.location.Location;

@ExtendWith(MockitoExtension.class)
class CenterJpaPersistenceAdapterTest {

    private static final Long CENTER_ID = 1L;
    private static final Long CLIENT_ID = 2L;
    private static final String CENTER_NAME = "Test Center";
    private static final String CENTER_ADDRESS = "123 Test St";
    private static final String CENTER_POSTAL_CODE = "12345";
    private static final Long LOCATION_ID = 3L;

    @Mock
    private CenterJpaPersistenceRepository repository;
    @Mock
    private CenterPersistenceMapper mapper;
    @InjectMocks
    private CenterJpaPersistenceAdapter adapter;

    private Center center;
    private CenterEntity centerEntity;

    @BeforeEach
    void setUp() {
        center = Center.builder()
                .id(CENTER_ID)
                .clientId(CLIENT_ID)
                .name(CENTER_NAME)
                .address(CENTER_ADDRESS)
                .postalCode(CENTER_POSTAL_CODE)
                .location(Location.from(LOCATION_ID))
                .build();

        CenterEntityId id = CenterEntityId.from(CENTER_ID, CLIENT_ID);
        centerEntity = new CenterEntity();
        centerEntity.setId(id);
        centerEntity.setName(CENTER_NAME);
        centerEntity.setAddress(CENTER_ADDRESS);
        centerEntity.setPostalCode(CENTER_POSTAL_CODE);
    }

    @Test
    @DisplayName("create should create new entity, save it, and return domain")
    void create_shouldCreateEntitySaveAndReturnDomain() {
        // Given
        given(repository.save(any(CenterEntity.class))).willReturn(centerEntity);
        given(mapper.toDomain(centerEntity)).willReturn(center);

        // When
        Center result = adapter.create(center);

        // Then
        then(mapper).should().update(any(CenterEntity.class), eq(center));
        then(repository).should().save(any(CenterEntity.class));
        then(mapper).should().toDomain(centerEntity);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("read by id should find entity and map to domain")
    void read_shouldFindByIdAndMapToDomain() {
        // Given
        CenterEntityId id = CenterEntityId.from(CENTER_ID, CLIENT_ID);
        given(repository.findById(id)).willReturn(Optional.of(centerEntity));
        given(mapper.toDomain(centerEntity)).willReturn(center);

        // When
        Optional<Center> result = adapter.read(id);

        // Then
        then(repository).should().findById(id);
        then(mapper).should().toDomain(centerEntity);
        assertThat(result.isPresent());
    }

    @Test
    @DisplayName("read by id should return empty when entity not found")
    void read_shouldReturnEmpty_whenEntityNotFound() {
        // Given
        CenterEntityId id = CenterEntityId.from(CENTER_ID, CLIENT_ID);
        given(repository.findById(id)).willReturn(Optional.empty());

        // When
        Optional<Center> result = adapter.read(id);

        // Then
        then(repository).should().findById(id);
        then(mapper).should(never()).toDomain(any(CenterEntity.class));
        assertThat(result.isEmpty());
    }

    @Test
    @DisplayName("read by id and clientId should use specification and map to domain")
    void read_shouldFindByIdAndClientIdUsingSpecification() {
        // Given
        CenterEntityId id = CenterEntityId.from(CENTER_ID, CLIENT_ID);
        given(repository.findById(id)).willReturn(Optional.of(centerEntity));
        given(mapper.toDomain(centerEntity)).willReturn(center);

        // When
        Optional<Center> result = adapter.read(CENTER_ID, CLIENT_ID);

        // Then
        then(repository).should().findById(id);
        then(mapper).should().toDomain(centerEntity);
        assertThat(result.isPresent());
    }

    @Test
    @DisplayName("read by id and clientId should return empty when not found")
    void read_shouldReturnEmpty_whenNotFoundByIdAndClientId() {
        // Given
        CenterEntityId id = CenterEntityId.from(CENTER_ID, CLIENT_ID);
        given(repository.findById(id)).willReturn(Optional.empty());

        // When
        Optional<Center> result = adapter.read(CENTER_ID, CLIENT_ID);

        // Then
        then(repository).should().findById(id);
        then(mapper).should(never()).toDomain(any(CenterEntity.class));
        assertThat(result.isEmpty());
    }

    @Test
    @DisplayName("update should find entity, update it, and return domain")
    void update_shouldFindEntityUpdateAndReturnDomain() {
        // Given
        CenterEntityId id = CenterEntityId.from(CENTER_ID, CLIENT_ID);
        given(repository.findById(id)).willReturn(Optional.of(centerEntity));
        given(repository.save(centerEntity)).willReturn(centerEntity);
        given(mapper.toDomain(centerEntity)).willReturn(center);

        // When
        Center result = adapter.update(center);

        // Then
        then(repository).should().findById(id);
        then(mapper).should().update(centerEntity, center);
        then(mapper).should().toDomain(centerEntity);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("update should throw ResourceNotFoundException when entity not found")
    void update_shouldThrowException_whenEntityNotFound() {
        // Given
        CenterEntityId id = CenterEntityId.from(CENTER_ID, CLIENT_ID);

        given(repository.findById(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> adapter.update(center));
        then(repository).should().findById(id);
        then(mapper).should(never()).update(any(CenterEntity.class), any(Center.class));
    }

    @Test
    @DisplayName("delete by id should call repository deleteById")
    void delete_shouldCallRepositoryDeleteById() {
        // Given
        CenterEntityId id = CenterEntityId.from(CENTER_ID, CLIENT_ID);
        doNothing().when(repository).deleteById(id);

        // When
        adapter.delete(CENTER_ID, CLIENT_ID);

        // Then
        then(repository).should().deleteById(id);
    }

    @Test
    @DisplayName("search should use specification and map results to domain")
    void search_shouldUseSpecificationAndMapResults() {
        // Given
        CenterSearch criteria = CenterSearch.builder().name("Test").build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<CenterEntity> entityPage = new PageImpl<>(List.of(centerEntity), pageable, 1);

        given(repository.findAll(any(Specification.class), eq(pageable))).willReturn(entityPage);
        given(mapper.toDomain(centerEntity)).willReturn(center);

        // When
        Page<Center> result = adapter.search(criteria, pageable);

        // Then
        then(repository).should().findAll(any(Specification.class), eq(pageable));
        then(mapper).should().toDomain(centerEntity);
        assertThat(result).isNotNull();
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("search should return empty page when no results found")
    void search_shouldReturnEmptyPage_whenNoResultsFound() {
        // Given
        CenterSearch criteria = CenterSearch.builder().name("NonExistent").build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<CenterEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        given(repository.findAll(any(Specification.class), eq(pageable))).willReturn(emptyPage);

        // When
        Page<Center> result = adapter.search(criteria, pageable);

        // Then
        then(repository).should().findAll(any(Specification.class), eq(pageable));
        then(mapper).should(never()).toDomain(any(CenterEntity.class));
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("search should handle multiple results")
    void search_shouldHandleMultipleResults() {
        // Given
        CenterSearch criteria = CenterSearch.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        CenterEntityId id1 = CenterEntityId.from(1L, 2L);
        CenterEntity entity1 = new CenterEntity();
        entity1.setId(id1);
        CenterEntityId id2 = CenterEntityId.from(10L, 20L);
        CenterEntity entity2 = new CenterEntity();
        entity2.setId(id2);

        Page<CenterEntity> entityPage = new PageImpl<>(List.of(entity1, entity2), pageable, 2);

        Center center1 = Center.builder()
                .id(1L)
                .clientId(CLIENT_ID)
                .name("Center 1")
                .address("Address 1")
                .postalCode("12345")
                .location(Location.from(LOCATION_ID))
                .build();
        Center center2 = Center.builder()
                .id(2L)
                .clientId(CLIENT_ID)
                .name("Center 2")
                .address("Address 2")
                .postalCode("54321")
                .location(Location.from(LOCATION_ID))
                .build();

        given(repository.findAll(any(Specification.class), eq(pageable))).willReturn(entityPage);
        given(mapper.toDomain(entity1)).willReturn(center1);
        given(mapper.toDomain(entity2)).willReturn(center2);

        // When
        Page<Center> result = adapter.search(criteria, pageable);

        // Then
        then(repository).should().findAll(any(Specification.class), eq(pageable));
        then(mapper).should().toDomain(entity1);
        then(mapper).should().toDomain(entity2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }
}
