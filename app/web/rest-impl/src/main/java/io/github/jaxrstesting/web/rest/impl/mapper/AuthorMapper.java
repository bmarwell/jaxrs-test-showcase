package io.github.jaxrstesting.web.rest.impl.mapper;

import io.github.jaxrstesting.services.api.Author;
import io.github.jaxrstesting.web.rest.api.value.AuthorRestDto;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;

@Default
@Dependent
public class AuthorMapper {

  public AuthorRestDto mapToDto(Author serviceAuthor) {
    return new AuthorRestDto(
        serviceAuthor.id(),
        serviceAuthor.firstName(),
        serviceAuthor.lastName(),
        serviceAuthor.birthDate(),
        serviceAuthor.profession(),
        serviceAuthor.nicknames());
  }

  public Author mapToAuthor(AuthorRestDto restDto) {
    return new Author(
        restDto.id(),
        restDto.firstName(),
        restDto.lastName(),
        restDto.birthDate(),
        restDto.profession(),
        restDto.nicknames());
  }
}
