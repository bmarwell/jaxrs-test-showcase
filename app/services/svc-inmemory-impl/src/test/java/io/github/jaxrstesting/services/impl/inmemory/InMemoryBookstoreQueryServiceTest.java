package io.github.jaxrstesting.services.impl.inmemory;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.jaxrstesting.common.value.AuthorId;
import io.github.jaxrstesting.services.api.Author;
import io.github.jaxrstesting.services.api.query.AuthorByIdQuery;
import io.github.jaxrstesting.services.api.query.ComplexAuthorQuery;
import io.github.jaxrstesting.services.api.query.expressions.AndExpression;
import io.github.jaxrstesting.services.api.query.expressions.AuthorMatchExpression;
import io.github.jaxrstesting.services.api.query.expressions.OrExpression;
import io.github.jaxrstesting.services.impl.inmemory.mapper.AuthorMapper;
import io.github.jaxrstesting.services.impl.inmemory.repository.AuthorPdo;
import io.github.jaxrstesting.services.impl.inmemory.repository.AuthorRepository;
import io.github.jaxrstesting.services.impl.inmemory.repository.CriteriaAuthorRepository;
import io.github.jaxrstesting.services.impl.inmemory.repository.ImmutableAuthorPdo;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InMemoryBookstoreQueryServiceTest {

  final InMemoryBookstoreQueryService service = new InMemoryBookstoreQueryService();

  @BeforeEach
  void setUp() {
    final CriteriaAuthorRepository repository = new CriteriaAuthorRepository();
    testData(repository);

    service.setRepository(repository);
    service.setAuthorMapper(new AuthorMapper());
  }

  private static void testData(AuthorRepository repository) {
    repository.insertAll(Arrays.stream(TestData.AUTHORS).toList());
  }

  @Test
  void can_query_by_id() {
    // given
    var query = new AuthorByIdQuery(new AuthorId("rpfeynman"));

    // when:
    final List<Author> authors = service.queryAuthors(query).toList();

    // then:
    assertThat(authors)
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void can_query_by_two_ids_or() {
    // given:
    final AuthorMatchExpression matchId1 = new AuthorMatchExpression("id", AuthorId.fromString("rpfeynman"));
    final AuthorMatchExpression matchId2 = new AuthorMatchExpression("id", AuthorId.fromString("2"));
    final ComplexAuthorQuery query = new ComplexAuthorQuery(new OrExpression(List.of(matchId1, matchId2)));

    // when
    final List<Author> authors = service.queryAuthors(query).toList();

    // then
    assertThat(authors)
        .isNotEmpty()
        .hasSize(2);
  }

  @Test
  void query_by_two_ids_and_returns_null() {
    // given:
    final AuthorMatchExpression matchId1 = new AuthorMatchExpression("id", AuthorId.fromString("rpfeynman"));
    final AuthorMatchExpression matchId2 = new AuthorMatchExpression("id", AuthorId.fromString("2"));
    final ComplexAuthorQuery query = new ComplexAuthorQuery(new AndExpression(List.of(matchId1, matchId2)));

    // when
    final List<Author> authors = service.queryAuthors(query).toList();

    // then
    assertThat(authors)
        .isEmpty();
  }

  @Test
  void complex_nested_query_by_first_name_or_first_name_and_last_name() {
    var richardAndW = new AndExpression(List.of(
        new AuthorMatchExpression("firstName", "Richard"),
        new AuthorMatchExpression("lastName", "von Weizsäcker")));
    var inventor = new AuthorMatchExpression("profession", "inventor");
    final ComplexAuthorQuery query = new ComplexAuthorQuery(
        new OrExpression(List.of(
            richardAndW,
            inventor)));

    // when
    final List<Author> authors = service.queryAuthors(query).toList();

    // then
    assertThat(authors)
        .hasSize(2)
        .extracting(author -> author.id().value())
        .contains("rvw", "2");
  }

  @Test
  void complex_nested_and_query_first_name_profession() {
    var firstName = new AuthorMatchExpression("firstName", "Richard");
    var politician = new AuthorMatchExpression("profession", "politician");
    final ComplexAuthorQuery query = new ComplexAuthorQuery(
        new AndExpression(List.of(
            firstName,
            politician)));

    // when
    final List<Author> authors = service.queryAuthors(query).toList();

    // then
    assertThat(authors)
        .hasSize(1)
        .extracting(author -> author.id().value())
        .contains("rvw");
  }

  static final class TestData {

    static final AuthorPdo[] AUTHORS = {
        ImmutableAuthorPdo.builder()
            .id(AuthorId.fromString("rpfeynman"))
            .firstName("Richard")
            .lastName("Feynman")
            .birthDate(LocalDate.of(1918, 5, 11))
            .profession("physicist")
            .addNicknames("Dick")
            .build(),
        ImmutableAuthorPdo.builder()
            .id(AuthorId.fromString("rvw"))
            .firstName("Richard")
            .lastName("von Weizsäcker")
            .birthDate(LocalDate.of(1920, 4, 15))
            .profession("politician")
            .build(),
        ImmutableAuthorPdo.builder()
            .id(AuthorId.fromString("2"))
            .firstName("Никола")
            .lastName("Тесла")
            .birthDate(LocalDate.of(1856, 7, 10))
            .profession("inventor")
            .addNicknames("Nikola")
            .build(),
        ImmutableAuthorPdo.builder()
            .id(AuthorId.fromString("ΜΔ"))
            .firstName("Μαργαρίτης")
            .lastName("Δήμητσας")
            .birthDate(LocalDate.of(1830, 1, 1))
            .profession("philologist")
            .addNicknames("Margaritis Dimitsas")
            .build()
    };
  }
}
