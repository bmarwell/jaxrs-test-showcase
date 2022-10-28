package io.github.jaxrstesting.common.value;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BookIdTest {

  @SuppressWarnings("ConstantConditions")
  @Test
  void bookId_has_fromString_method() {
    // given
    var bookIdString = UUID.randomUUID().toString();

    // when
    var bookIdFromString = BookId.fromString(bookIdString);
    var bookId = new BookId(bookIdString);

    // then
    assertThat(bookIdFromString)
        .isEqualTo(bookId)
        .matches(bidc -> bidc instanceof ValueWrapper<String>)
        .extracting("value")
        .isEqualTo(bookIdString);
  }
}
