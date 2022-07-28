package io.github.jaxrstesting.systemtests.jgj;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.jaxrstesting.services.api.Author;
import io.github.jaxrstesting.services.api.BookstoreQueryService;
import io.github.jaxrstesting.services.api.query.AuthorByIdQuery;
import io.github.jaxrstesting.services.api.query.AuthorQuery;
import io.github.jaxrstesting.systemtests.jaxrs.jsonb.support.JsonbJsonMessageBodyWriter;
import io.github.jaxrstesting.web.rest.api.json.JsonbContext;
import io.github.jaxrstesting.web.rest.impl.AuthorResourceImpl;
import io.github.jaxrstesting.web.rest.impl.mapper.AuthorMapper;
import jakarta.json.bind.Jsonb;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import java.util.stream.Stream;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AuthorResourceTest extends JerseyTest {

  private static final BookstoreQueryService queryService = mock(BookstoreQueryService.class);

  private static final JsonbContext jsonbContext = new JsonbContext();

  @AfterAll
  public static void closeJsonbContext() throws Exception {
    jsonbContext.close();
  }

  public static class ApplicationBinder extends AbstractBinder {

    @Override
    protected void configure() {
      bind(queryService).to(BookstoreQueryService.class).ranked(1);
      bind(AuthorMapper.class).to(AuthorMapper.class).ranked(1);
      bind(jsonbContext.getJsonb()).to(Jsonb.class);
    }
  }

  @Override
  protected Application configure() {
    // Find first available port.
    forceSet(TestProperties.CONTAINER_PORT, "0");

    return new ResourceConfig(AuthorResourceImpl.class)
        .register(new ApplicationBinder())
        .register(new JsonbJsonMessageBodyWriter());
  }

  @Test
  public void author_by_id_returns_author() {
    // given
    when(queryService.queryAuthors(any(AuthorQuery.class)))
        .then(args -> makeAnswerFromQuery(args.getArgument(0)));

    // when
    final Response authorByIdResponse = target("authors/rpfeynman")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(Response.class);

    // then
    assertThat(authorByIdResponse)
        .hasFieldOrPropertyWithValue("status", 200)
        .extracting(rsp -> rsp.readEntity(String.class))
        .extracting(jsonStr -> jsonbContext.getJsonb().fromJson(jsonStr, Map.class))
        .hasFieldOrPropertyWithValue("id", "rpfeynman");
  }

  private Stream<Author> makeAnswerFromQuery(AuthorQuery argument) {
    if (argument instanceof AuthorByIdQuery idQuery) {
      return Stream.of(new Author(idQuery.authorId()));
    }

    throw new UnsupportedOperationException("Not implemented class: " + argument.getClass());
  }

  @Test
  public void author_with_unknown_id_returns_404() {
    // given

    // when
    final Response authorByIdResponse = target("authors/anonymous")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(Response.class);

    // then
    assertThat(authorByIdResponse)
        .hasFieldOrPropertyWithValue("status", 404);
  }

}
