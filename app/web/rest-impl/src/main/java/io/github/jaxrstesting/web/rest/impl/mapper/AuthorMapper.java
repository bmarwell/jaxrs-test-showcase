package io.github.jaxrstesting.web.rest.impl.mapper;

import io.github.jaxrstesting.web.rest.api.value.AuthorRestDto;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthorMapper {

  public AuthorRestDto mapToAuthor(io.github.jaxrstesting.services.api.Author serviceAuthor) {
    return new AuthorRestDto(serviceAuthor.id());
  }
}
