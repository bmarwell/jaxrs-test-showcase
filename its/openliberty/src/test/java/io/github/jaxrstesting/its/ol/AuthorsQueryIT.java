package io.github.jaxrstesting.its.ol;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.jaxrstesting.common.value.AuthorId;
import io.github.jaxrstesting.web.rest.api.json.JsonbMessageBodyReader;
import io.github.jaxrstesting.web.rest.api.value.AuthorRestDto;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthorsQueryIT {

  protected URI getBaseUri() {
    return URI.create("http://localhost:" + System.getProperty("http.port") + "/" + System.getProperty("app.context.root"));
  }

  @Test
  void test_query_authors_by_id() {
    // given:
    AuthorId authorId = new AuthorId("rpfeynman");

    // when:
    final Response response = getClient().target(getBaseUri() + "/authors/" + authorId.value())
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    // then:
    assertThat(response)
        .matches(rs -> rs.getStatus() == 200);
    final AuthorRestDto authorRestDto = response.readEntity(AuthorRestDto.class);
    assertThat(authorRestDto)
        .extracting(AuthorRestDto::id, AuthorRestDto::firstName)
        .contains(authorId, "Richard");
  }

  private static Client getClient() {
    return ClientBuilder.newClient()
        .register(JsonbMessageBodyReader.class);
  }
}
