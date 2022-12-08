package io.github.jaxrstesting.services.impl.inmemory.mapper;

import io.github.jaxrstesting.services.api.Author;
import io.github.jaxrstesting.services.impl.inmemory.repository.AuthorPdo;
import io.github.jaxrstesting.services.impl.inmemory.repository.ImmutableAuthorPdo;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;
import java.io.Serializable;

@Dependent
@Default
public class AuthorMapper implements Serializable {

  public AuthorPdo toRepository(io.github.jaxrstesting.services.api.Author author) {
    return ImmutableAuthorPdo.builder()
        .id(author.id())
        .firstName(author.firstName())
        .lastName(author.lastName())
        .birthDate(author.birthDate())
        .profession(author.profession())
        .addAllNicknames(author.nicknames())
        .build();
  }

  public Author fromRepository(AuthorPdo authorPdo) {
    return new Author(
        authorPdo.id(),
        authorPdo.firstName(),
        authorPdo.lastName(),
        authorPdo.birthDate(),
        authorPdo.profession(),
        authorPdo.nicknames());
  }
}
