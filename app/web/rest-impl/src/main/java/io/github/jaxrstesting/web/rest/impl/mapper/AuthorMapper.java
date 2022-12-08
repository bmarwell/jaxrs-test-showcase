package io.github.jaxrstesting.web.rest.impl.mapper;

import static java.util.Objects.requireNonNull;

import io.github.jaxrstesting.services.api.Author;
import io.github.jaxrstesting.web.rest.api.value.AuthorRestDto;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;

@Default
@Dependent
public class AuthorMapper {

  public AuthorRestDto mapToDto(Author serviceAuthor) {
    return new AuthorRestDto(
        requireNonNull(serviceAuthor.id()),
        requireNonNull(serviceAuthor.firstName()),
        serviceAuthor.lastName(),
        serviceAuthor.birthDate(),
        serviceAuthor.profession(),
        serviceAuthor.nicknames());
  }

  public Author mapToAuthor(AuthorRestDto restDto) {
    return new Author(
        requireNonNull(restDto.id()),
        requireNonNull(restDto.firstName()),
        requireNonNull(restDto.lastName()),
        requireNonNull(restDto.birthDate()),
        requireNonNull(restDto.profession()),
        requireNonNull(restDto.nicknames()));
  }
}
