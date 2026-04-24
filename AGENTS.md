## Testing

- **Black-Box Testing**: Prioritize testing system behavior and output over internal implementation details.
- **State-Based Assertions**: Verify the system reaches the correct state. Avoid interaction testing (e.g., avoid `verify(mock).method()`).
- **Realism over Isolation**: Minimize mocks. Build real objects and use in-memory databases whenever possible. Use the Builder Pattern for object creation instead of mocking data structures.
- **Libraries**:
    - Framework: **JUnit 5** (Jupiter). Do not use JUnit 4.
    - Mocking: **Mockito** (only when real objects are not feasible).
    - **BDD Style**: Use `then().should()` instead of `when().then()`. 
    - Assertions: **AssertJ**. Always use static imports for `assertThat` (`import static org.assertj.core.api.Assertions.assertThat;`).

### Test Types and Annotations
- **Unit test**:
    - Use plain Java (no Spring context). Use `@Mock` and `@InjectMocks` for dependencies.
    - Test domain logic.
    - File path: `src/test/java/...`
- **Presentation layer slice test**:
    - Use `@WebMvcTest(ControllerClass.class)`.
    - Use `@MockBean` to mock underlying services.
    - File path: `src/test/java/...`
- **Persistence Layer slice test**:
    - Use `@DataJpaTest`.
    - This configures an in-memory database.
    - File path: `src/test/java/...`
- **Integration Test**:
    - Use `@SpringBootTest` ONLY when needed to load context and external integrations (Avoid Integration Overhead).
    - Use `@Sql` to initialize required data. Create separate SQL files in `src/test/resources/<domain>/`, where `<domain>` is the model of the register.
    - File path: `src/test-integration/java/...` (or your specific integration source set).

### Conventions
- **Class naming**: 
    - Unit, Presentation, and Persistence test classes end with `Test` (e.g., `BookTest`).
    - Integration test classes end with `IT` (e.g., `BookIT`).
- **Method naming**: Use `snake_case` formatted as: `[method]_[expectedBehavior]_[scenario]`. 
    - Example: `update_shouldReturn400BadRequest_whenInvalidInput()`
- **Structure**: Tests MUST strictly follow the BDD pattern using `// Given`, `// When`, `// Then` comments.
- **BDD Style**: Use mockito BDD style syntax `given(mock.method()).willReturn(value)` and `then(mock).should().method()`.
- **Constraints**: 
    - Avoid using Reflection in tests.
    - Chain AssertJ assertions fluently (e.g., `assertThat(result).isNotNull().hasSize(3);`).

### Structure Example
```java
@Test
void calculateTotal_shouldReturnCorrectAmount_whenValidItemsProvided() {
    // Given
    Order order = OrderBuilder.aValidOrder().withItems(2).build();
    
    // When
    BigDecimal total = orderService.calculateTotal(order);
    
    // Then
    assertThat(total)
        .isNotNull()
        .isEqualByComparingTo("150.00");
}
```