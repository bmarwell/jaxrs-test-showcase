package io.github.jaxrstesting.web.rest.api.value;

import jakarta.json.bind.annotation.JsonbCreator;

public record AuthorId(String value) implements JsonValueWrapper<String> {

  @JsonbCreator
  public static AuthorId fromString(String value) {
    return new AuthorId(value);
  }
}
