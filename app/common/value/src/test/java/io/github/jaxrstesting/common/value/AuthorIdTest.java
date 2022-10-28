package io.github.jaxrstesting.common.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthorIdTest {

  @SuppressWarnings("ConstantConditions")
  @Test
  void author_id_has_fromString_method() {
    // given
    var authorIdString = "rpfeynman";

    // when
    var authorIdFromString = AuthorId.fromString(authorIdString);
    var authorId = new AuthorId(authorIdString);

    // then
    assertThat(authorIdFromString)
        .isEqualTo(authorId)
        .matches(aidc -> aidc instanceof ValueWrapper<String>)
        .extracting("value")
        .isEqualTo(authorIdString);
  }
}
