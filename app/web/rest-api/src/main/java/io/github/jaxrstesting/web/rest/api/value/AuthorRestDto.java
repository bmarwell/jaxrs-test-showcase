package io.github.jaxrstesting.web.rest.api.value;

import io.github.jaxrstesting.common.value.AuthorId;
import jakarta.json.bind.annotation.JsonbCreator;
import java.time.LocalDate;
import java.util.List;

public record AuthorRestDto(AuthorId id,
    String firstName,
    String lastName,
    LocalDate birthDate,
    String profession,
    List<String> nicknames) {

  @JsonbCreator
  public AuthorRestDto {
  }
}
