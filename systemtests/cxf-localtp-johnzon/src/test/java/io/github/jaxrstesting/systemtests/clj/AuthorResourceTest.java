package io.github.jaxrstesting.systemtests.clj;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.jaxrstesting.services.api.Author;
import io.github.jaxrstesting.services.api.BookstoreQueryService;
import io.github.jaxrstesting.services.api.query.AuthorByIdQuery;
import io.github.jaxrstesting.services.api.query.AuthorQuery;
import io.github.jaxrstesting.systemtests.cxf.junit.CxfLocalTransportExtension;
import io.github.jaxrstesting.web.rest.api.json.JsonbContext;
import io.github.jaxrstesting.web.rest.impl.AuthorResourceImpl;
import io.github.jaxrstesting.web.rest.impl.mapper.AuthorMapper;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.cxf.jaxrs.provider.jsrjsonb.JsrJsonbProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AuthorResourceTest {


  private static final AuthorResourceImpl authorResource = new AuthorResourceImpl();

  private static final BookstoreQueryService queryService = mock(BookstoreQueryService.class);

  private static final AuthorMapper mapper = new AuthorMapper();

  private static final JsonbContext jsonbContext = new JsonbContext();


  @RegisterExtension
  static CxfLocalTransportExtension ltp = new CxfLocalTransportExtension()
      .withResource(AuthorResourceImpl.class, authorResource)
      .withProvider(new JsrJsonbProvider(jsonbContext.getJsonb()));

  @BeforeAll
  public static void setUpResource() {
    authorResource.setQueryService(queryService);
    authorResource.setAuthorMapper(mapper);
  }

  @AfterAll
  public static void closeJsonbContext() throws Exception {
    jsonbContext.close();
  }

  @Test
  public void author_by_id_returns_author() {
    // given
    when(queryService.queryAuthors(any(AuthorQuery.class)))
        .then(args -> makeAnswerFromQuery(args.getArgument(0)));

    // when
    final Response authorByIdResponse = ltp.getWebClient()
        .accept("application/json")
        .path("authors/rpfeynman")
        .get();

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
    final Response authorByIdResponse = ltp.getWebClient()
        .accept(MediaType.APPLICATION_JSON_TYPE)
        .path("authors/anonymous")
        .get();

    // then
    assertThat(authorByIdResponse)
        .hasFieldOrPropertyWithValue("status", 404);
  }

}
