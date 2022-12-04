package io.github.jaxrstesting.systemtests.reuj;

import io.github.jaxrstesting.services.api.Author;
import io.github.jaxrstesting.services.api.BookstoreQueryService;
import io.github.jaxrstesting.services.api.query.AuthorByIdQuery;
import io.github.jaxrstesting.services.api.query.AuthorQuery;
import io.github.jaxrstesting.web.rest.api.json.JsonbContext;
import io.github.jaxrstesting.web.rest.impl.AuthorResourceImpl;
import io.github.jaxrstesting.web.rest.impl.mapper.AuthorMapper;
import io.undertow.servlet.api.DeploymentInfo;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.test.TestPortProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class AuthorResourceTest {

  private static final JsonbContext jsonbContext = new JsonbContext();

  private static final BookstoreQueryService queryService = Mockito.mock(BookstoreQueryService.class);

  private static UndertowJaxrsServer server;

  @BeforeAll
  public static void init() {
    server = new UndertowJaxrsServer().start();
    ResteasyDeployment deployment = new ResteasyDeploymentImpl();
    final AuthorResourceImpl authorResource = new AuthorResourceImpl();
    authorResource.setQueryService(queryService);
    authorResource.setAuthorMapper(new AuthorMapper());
    deployment.setResources(List.of(authorResource));
    deployment.setProviders(List.of(jsonbContext));

    DeploymentInfo di = server.undertowDeployment(deployment, "/");
    di.setContextPath("/");
    di.setDeploymentName("Books and their Authors");
    di.setClassLoader(Application.class.getClassLoader());

    server.deploy(di);
  }

  @AfterAll
  public static void stop() {
    server.stop();
  }

  @Test
  public void author_by_id_returns_author() {
    // given
    Client client = ClientBuilder.newClient();
    Mockito.when(queryService.queryAuthors(ArgumentMatchers.any(AuthorQuery.class)))
        .then(args -> makeAnswerFromQuery(args.getArgument(0)));

    // when
    final Response authorByIdResponse = client.target(TestPortProvider.generateURL("/authors/rpfeynman"))
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(Response.class);

    // then
    Assertions.assertThat(authorByIdResponse)
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
    Client client = ClientBuilder.newClient();

    // when
    final Response authorByIdResponse = client.target(TestPortProvider.generateURL("/authors/anonymous"))
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(Response.class);

    // then
    Assertions.assertThat(authorByIdResponse)
        .hasFieldOrPropertyWithValue("status", 404);
  }


}
