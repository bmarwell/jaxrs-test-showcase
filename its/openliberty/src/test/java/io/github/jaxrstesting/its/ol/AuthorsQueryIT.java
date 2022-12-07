package io.github.jaxrstesting.its.ol;

import static org.assertj.core.api.Assertions.assertThat;

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
    final Client client = ClientBuilder.newClient();

    final Response response = client.target(getBaseUri() + "/authors/rpfeynman")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertThat(response)
        .matches(rs -> rs.getStatus() == 200);
  }
}
