package io.github.jaxrstesting.services.api;

import io.github.jaxrstesting.services.api.query.AuthorQuery;
import java.util.stream.Stream;

public interface BookstoreQueryService {

  Stream<Author> queryAuthors(AuthorQuery authorQuery);
}
