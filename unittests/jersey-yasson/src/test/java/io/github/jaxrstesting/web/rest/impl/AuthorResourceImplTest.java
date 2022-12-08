package io.github.jaxrstesting.web.rest.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.jaxrstesting.common.value.AuthorId;
import io.github.jaxrstesting.services.api.Author;
import io.github.jaxrstesting.services.api.BookstoreQueryService;
import io.github.jaxrstesting.services.api.query.AuthorByIdQuery;
import io.github.jaxrstesting.services.api.query.AuthorQuery;
import io.github.jaxrstesting.web.rest.api.value.AuthorRestDto;
import io.github.jaxrstesting.web.rest.impl.mapper.AuthorMapper;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

@DisabledIfSystemProperty(
    named = "jakarta.ws.rs.ext.RuntimeDelegate",
    matches = "org.apache.cxf.jaxrs.impl.RuntimeDelegateImpl",
    disabledReason = "CXF cannot set jakarta.ws.rs.ext.RuntimeDelegate.")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthorResourceImplTest {

  public final BookstoreQueryService queryService = mock(BookstoreQueryService.class);

  public final AuthorResourceImpl authorResource = new AuthorResourceImpl();

  @BeforeEach
  public void setUpMock() {
    this.authorResource.setQueryService(queryService);
    this.authorResource.setAuthorMapper(new AuthorMapper());
    final String sysProp = System.getProperty("jakarta.ws.rs.ext.RuntimeDelegate");
  }

  /**
   * This test does test querying the service (which is mocked), but it lacks support for the
   * annotations provided in both the API and implementation classes. I.e. you will neither test the
   * Path parameters nor the Produces/Consumes annotations.
   */
  @Test
  public void query_by_authorId_returns_author() {
    // given
    final var authorId = new AuthorId("rpfeynman");
    final var expected = new AuthorRestDto(
        authorId,
        "Richard",
        "Feynman",
        LocalDate.of(1918, 5, 11),
        "physicist",
        List.of("Dick"));

    when(queryService.queryAuthors(any(AuthorQuery.class)))
        .then(args -> makeAnswerFromQuery(args.getArgument(0)));

    // when
    final Response authorByIdResponse = authorResource.getAuthorById(authorId);

    // then
    assertThat(authorByIdResponse)
        .extracting(Response::getStatus, Response::getEntity)
        .containsExactly(200, expected);
  }

  private Stream<Author> makeAnswerFromQuery(AuthorQuery argument) {
    if (argument instanceof AuthorByIdQuery idQuery) {
      return Stream.of(new Author(
          idQuery.authorId(),
          "Richard",
          "Feynman",
          LocalDate.of(1918, 5, 11),
          "physicist",
          List.of("Dick")));
    }

    throw new UnsupportedOperationException("Not implemented class: " + argument.getClass());
  }
}
