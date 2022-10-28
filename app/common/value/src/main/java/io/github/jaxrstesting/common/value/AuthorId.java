package io.github.jaxrstesting.common.value;

/**
 * The ID class of an author entry.
 * 
 * @param value the ID of an author entry as string.
 */
public record AuthorId(String value) implements ValueWrapper<String> {

  public static AuthorId fromString(String value) {
    return new AuthorId(value);
  }
}
