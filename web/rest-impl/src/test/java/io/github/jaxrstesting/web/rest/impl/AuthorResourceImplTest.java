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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthorResourceImplTest {

  public final BookstoreQueryService queryService = mock(BookstoreQueryService.class);

  public final AuthorResourceImpl authorResource = new AuthorResourceImpl();

  @BeforeEach
  public void setUpMock() {
    this.authorResource.setQueryService(queryService);
    this.authorResource.setAuthorMapper(new AuthorMapper());
    final String sysProp = System.getProperty("jakarta.ws.rs.ext.RuntimeDelegate");
    Logger.getLogger(AuthorResourceImplTest.class.getName()).log(Level.WARNING, sysProp);
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
    final var expected = new AuthorRestDto(authorId);

    //noinspection SwitchStatementWithTooFewBranches
    when(queryService.queryAuthors(any(AuthorQuery.class)))
        .then(
            args ->
                switch (args.getArgument(0)) {
                  case AuthorByIdQuery authorByIdQuery -> Stream.of(
                      new Author(authorByIdQuery.authorId()));
                  default -> throw new UnsupportedOperationException(
                      "not implemented: " + args.getArgument(0).getClass());
                });

    // when
    final Response authorByIdResponse = authorResource.getAuthorById(authorId);

    // then
    assertThat(authorByIdResponse)
        .extracting(Response::getStatus, Response::getEntity)
        .containsExactly(200, expected);
  }
}
