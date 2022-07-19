package io.github.jaxrstesting.web.rest.api.value;

public record BookId(String value) implements JsonValueWrapper<String> {

  public static BookId fromString(String value) {
    return new BookId(value);
  }
}
