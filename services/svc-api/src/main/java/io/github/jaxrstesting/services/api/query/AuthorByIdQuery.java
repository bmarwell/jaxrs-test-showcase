package io.github.jaxrstesting.services.api.query;

import io.github.jaxrstesting.common.value.AuthorId;

public record AuthorByIdQuery(AuthorId authorId) implements AuthorQuery {}
