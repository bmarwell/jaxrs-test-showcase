package io.github.jaxrstesting.common.value;

public record AuthorId(String value) implements ValueWrapper<String> {

  public static AuthorId fromString(String value) {
    return new AuthorId(value);
  }
}
