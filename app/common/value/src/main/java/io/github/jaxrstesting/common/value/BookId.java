package io.github.jaxrstesting.common.value;

public record BookId(String value) implements ValueWrapper<String> {

  public static BookId fromString(String value) {
    return new BookId(value);
  }
}
