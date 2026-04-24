package dev.jgregorio.demo.data.infrastructure.persistence.center;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.*;

import dev.jgregorio.demo.data.domain.center.CenterSearch;
import jakarta.persistence.criteria.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaFunction;
import org.hibernate.query.criteria.JpaPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CenterSpecificationTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Root<CenterEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock(extraInterfaces = HibernateCriteriaBuilder.class)
    private CriteriaBuilder cb;

    private HibernateCriteriaBuilder hcb;

    private static Stream<String> blankStrings() {
        return Stream.of(null, "", "   ");
    }

    @BeforeEach
    void setUp() {
        hcb = (HibernateCriteriaBuilder) cb;
    }

    @Test
    @DisplayName("Should throw IllegalStateException when trying to instantiate")
    void testPrivateConstructor() {
        assertThrows(
                IllegalStateException.class,
                () -> {
                    try {
                        Constructor<CenterSpecification> constructor =
                                CenterSpecification.class.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        constructor.newInstance();
                    } catch (InvocationTargetException e) {
                        throw e.getTargetException();
                    }
                });
    }

    @Test
    @DisplayName("Should create a name predicate when name is provided")
    void search_whenNameIsProvided_shouldCreateNamePredicate() {
        // Given
        String searchName = " Some Center ";
        CenterSearch criteria = CenterSearch.builder().name(searchName).build();
        Specification<CenterEntity> spec = CenterSpecification.search(criteria);
        Path<String> namePath = mock(Path.class);
        JpaFunction<String> lowerNamePath = mock(JpaFunction.class);
        JpaFunction<String> collatedPath = mock(JpaFunction.class);
        JpaPredicate likePredicate = mock(JpaPredicate.class);
        JpaPredicate finalPredicate = mock(JpaPredicate.class);
        when(root.get(CenterEntity_.name)).thenReturn(namePath);
        when(cb.lower(namePath)).thenReturn(lowerNamePath);
        when(hcb.collate(lowerNamePath, "BINARY_AI")).thenReturn(collatedPath);
        when(cb.like(collatedPath, "%some center%")).thenReturn(likePredicate);
        when(cb.and(likePredicate)).thenReturn(finalPredicate);
        // When
        Predicate result = spec.toPredicate(root, query, cb);
        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(finalPredicate);
        then(cb).lower(namePath);
        verify(hcb).should().collate(lowerNamePath, "BINARY_AI");
        then(cb).should().like(collatedPath, "%some center%");
        then(cb).and(likePredicate);
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    @DisplayName("Should not create a name predicate when name is blank")
    void search_whenNameIsBlank_shouldNotCreateNamePredicate(String blankName) {
        // Given
        CenterSearch criteria = CenterSearch.builder().name(blankName).build();
        Specification<CenterEntity> spec = CenterSpecification.search(criteria);
        Predicate finalPredicate = mock(JpaPredicate.class);
        when(cb.and()).should().thenReturn(finalPredicate);
        // When
        Predicate result = spec.toPredicate(root, query, cb);
        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(finalPredicate);
        then(root).should(never()).get(anyString());
        then(cb).should(never()).lower(any());
        then(hcb).should(never()).collate(any(), any());
        then(cb).should(never()).like(any(), anyString());
        then(cb).and();
    }

    @Test
    @DisplayName("Should create an address predicate when address is provided")
    void search_whenAddressIsProvided_shouldCreateAddressPredicate() {
        // Given
        String searchAddress = "Some Address";
        CenterSearch criteria = CenterSearch.builder().address(searchAddress).build();
        Specification<CenterEntity> spec = CenterSpecification.search(criteria);
        Path<String> addressPath = mock(Path.class);
        JpaFunction<String> lowerAddressPath = mock(JpaFunction.class);
        JpaFunction<String> collatedPath = mock(JpaFunction.class);
        JpaPredicate likePredicate = mock(JpaPredicate.class);
        JpaPredicate finalPredicate = mock(JpaPredicate.class);

        when(root.get(CenterEntity_.address)).thenReturn(addressPath);
        when(cb.lower(addressPath)).should().thenReturn(lowerAddressPath);
        when(hcb.collate(lowerAddressPath, "BINARY_AI")).thenReturn(collatedPath);
        when(cb.like(collatedPath, "%some address%")).thenReturn(likePredicate);
        when(cb.and(likePredicate)).thenReturn(finalPredicate);

        // When
        Predicate result = spec.toPredicate(root, query, cb);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(finalPredicate);
        then(cb).lower(addressPath);
        verify(hcb).should().collate(lowerAddressPath, "BINARY_AI");
        then(cb).should().like(collatedPath, "%some address%");
        then(cb).and(likePredicate);
    }

    @Test
    @DisplayName("Should create a postal code predicate when postal code is provided")
    void search_whenPostalCodeIsProvided_shouldCreatePostalCodePredicate() {
        // Given
        String searchPostalCode = "12345";
        CenterSearch criteria = CenterSearch.builder().postalCode(searchPostalCode).build();
        Specification<CenterEntity> spec = CenterSpecification.search(criteria);
        Path<String> postalCodePath = mock(Path.class);
        JpaFunction<String> lowerPostalCodePath = mock(JpaFunction.class);
        JpaFunction<String> collatedPath = mock(JpaFunction.class);
        JpaPredicate likePredicate = mock(JpaPredicate.class);
        JpaPredicate finalPredicate = mock(JpaPredicate.class);

        when(root.get(CenterEntity_.postalCode)).thenReturn(postalCodePath);
        when(cb.lower(postalCodePath)).should().thenReturn(lowerPostalCodePath);
        when(hcb.collate(lowerPostalCodePath, "BINARY_AI")).thenReturn(collatedPath);
        when(cb.like(collatedPath, "%12345%")).thenReturn(likePredicate);
        when(cb.and(likePredicate)).thenReturn(finalPredicate);

        // When
        spec.toPredicate(root, query, cb);

        // Then
        then(cb).lower(postalCodePath);
        verify(hcb).should().collate(lowerPostalCodePath, "BINARY_AI");
        then(cb).should().like(collatedPath, "%12345%");
        then(cb).should().and(likePredicate);
    }
}
