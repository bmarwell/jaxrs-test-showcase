package io.github.jaxrstesting.services.api.query;

import io.github.jaxrstesting.common.value.AuthorId;

/**
 * Queries for an author by their id.
 * 
 * @param authorId the ID to query.
 */
public record AuthorByIdQuery(AuthorId authorId) implements AuthorQuery {
}
