package io.github.jaxrstesting.services.api;

import io.github.jaxrstesting.services.api.query.AuthorQuery;
import java.util.List;
import java.util.stream.Stream;

/**
 * Service for queries to the bookstore.
 */
public interface BookstoreQueryService {

  /**
   * Parses a complex query and returns a stream of matching authors.
   *
   * @param authorQuery a (potentially) complex query.
   * @return a {@code Stream} of matching authors.
   */
  Stream<Author> queryAuthors(AuthorQuery authorQuery);

  long insertAuthors(List<Author> authors);
}
