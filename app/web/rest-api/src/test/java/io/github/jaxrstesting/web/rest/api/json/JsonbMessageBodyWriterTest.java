package io.github.jaxrstesting.web.rest.api.json;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.jaxrstesting.common.value.AuthorId;
import io.github.jaxrstesting.web.rest.api.value.AuthorRestDto;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class JsonbMessageBodyWriterTest {

  @Test
  void writeTo() throws IOException {
    // given
    final JsonbMessageBodyWriter<AuthorRestDto> writer = new JsonbMessageBodyWriter<>();
    final ByteArrayOutputStream os = new ByteArrayOutputStream();
    final var authorId = new AuthorId("rpfeynman");
    final AuthorRestDto authorRestDto = new AuthorRestDto(
        authorId,
        "Richard",
        "Feynman",
        LocalDate.of(1918, 5, 11),
        "physicist",
        List.of("Dick"));

    // when
    writer.writeTo(authorRestDto, AuthorRestDto.class, null, null, MediaType.APPLICATION_JSON_TYPE, new MultivaluedHashMap<>(), os);
    os.flush();
    os.close();

    // then
    final var json = os.toString(StandardCharsets.UTF_8);
    assertThat(json)
        .contains("\"first_name\"");
  }

}
