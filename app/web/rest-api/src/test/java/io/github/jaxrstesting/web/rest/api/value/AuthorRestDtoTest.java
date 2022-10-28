package io.github.jaxrstesting.web.rest.api.value;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.jaxrstesting.common.value.AuthorId;
import io.github.jaxrstesting.web.rest.api.json.JsonbContext;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthorRestDtoTest {

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
  void authorRestDto_serializes_to_json() {
    // given
    final var authorId = new AuthorId("rpfeynman");
    final AuthorRestDto authorRestDto = new AuthorRestDto(authorId);

    // when
    final String json = jsonb.getJsonb().toJson(authorRestDto);

    // then
    // noinspection unchecked
    assertThat(jsonb.getJsonb().fromJson(json, Map.class)).containsEntry("id", "rpfeynman");
  }
}
