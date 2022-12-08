package io.github.jaxrstesting.web.rest.api.json;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.jaxrstesting.common.value.AuthorId;
import io.github.jaxrstesting.web.rest.api.value.AuthorRestDto;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class JsonbMessageBodyReaderTest {

  @Test
  void readFrom() throws IOException {
    // given
    var json = """
        {
            "birth_date": "1918-05-11",
            "first_name": "Richard",
            "id": "rpfeynman",
            "last_name": "Feynman",
            "nicknames": [
                "Dick"
            ],
            "profession": "physicist"
        }""";
    final ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    final JsonbMessageBodyReader<AuthorRestDto> reader = new JsonbMessageBodyReader<>();

    // when
    final AuthorRestDto author = reader.readFrom(
        AuthorRestDto.class, null, null, MediaType.APPLICATION_JSON_TYPE,
        new MultivaluedHashMap<>(), is);

    // then
    is.close();
    assertThat(author)
        .extracting(AuthorRestDto::id, AuthorRestDto::firstName)
        .contains(new AuthorId("rpfeynman"), "Richard");
  }
}
