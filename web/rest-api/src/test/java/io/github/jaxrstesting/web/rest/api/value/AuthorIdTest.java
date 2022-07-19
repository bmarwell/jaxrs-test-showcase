package io.github.jaxrstesting.web.rest.api.value;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.jaxrstesting.common.value.AuthorId;
import io.github.jaxrstesting.web.rest.api.json.JsonbContext;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthorIdTest {

  private static JsonbContext jsonb;

  @BeforeAll
  public static void setUp() {
    jsonb = new JsonbContext();
  }

  @AfterAll
  public static void tearDown() throws Exception {
    if (jsonb != null) {
      jsonb.close();
    }
  }

  @Test
  void json_Serialization_To_String() {
    // given
    final var authorId = AuthorId.fromString("rpfeynman");
    final Map<String, List<AuthorId>> authorResults = Map.of("results", List.of(authorId));

    // when
    final var jsonResultMap = jsonb.getJsonb().toJson(authorResults);

    // then
    final var jsonReader = Json.createReader(new StringReader(jsonResultMap));
    final JsonArray results = jsonReader.readObject().getJsonArray("results");
    assertThat(results).hasSize(1).element(0).isEqualTo(Json.createValue(authorId.value()));
  }
}
