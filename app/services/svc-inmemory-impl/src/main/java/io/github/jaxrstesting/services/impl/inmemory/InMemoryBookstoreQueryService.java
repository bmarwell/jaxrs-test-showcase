package io.github.jaxrstesting.services.impl.inmemory;

import io.github.jaxrstesting.services.api.Author;
import io.github.jaxrstesting.services.api.BookstoreQueryService;
import io.github.jaxrstesting.services.api.query.AuthorByIdQuery;
import io.github.jaxrstesting.services.api.query.AuthorQuery;
import io.github.jaxrstesting.services.api.query.ComplexAuthorQuery;
import io.github.jaxrstesting.services.impl.inmemory.mapper.AuthorMapper;
import io.github.jaxrstesting.services.impl.inmemory.repository.AuthorPdo;
import io.github.jaxrstesting.services.impl.inmemory.repository.AuthorPdoCriteria;
import io.github.jaxrstesting.services.impl.inmemory.repository.AuthorRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

@Dependent
public class InMemoryBookstoreQueryService implements BookstoreQueryService {

  @Inject
  private AuthorRepository repository;

  @Inject
  private AuthorMapper authorMapper;

  @Override
  public Stream<Author> queryAuthors(AuthorQuery authorQuery) {
    // TODO: replace with pattern-switch in Java 21.
    if (authorQuery instanceof AuthorByIdQuery idQuery) {
      return this.repository.find(AuthorPdoCriteria.authorPdo.id.is(idQuery.authorId())).fetch()
          .stream()
          .map(authorMapper::fromRepository);
    } else if (authorQuery instanceof ComplexAuthorQuery complexAuthorQuery) {
      var visitor = new ComplexAuthorQueryExpressionVisitor();
      visitor.visit(complexAuthorQuery);
      return this.repository.find(visitor.getCriteria()).fetch()
          .stream()
          .map(authorMapper::fromRepository);
    } else {
      throw new UnsupportedOperationException("Not supported: [" + authorQuery.getClass() + "].");
    }
  }

  @Override
  public long insertAuthors(List<Author> authors) {
    final List<AuthorPdo> authorPdos = authors.stream()
        .map(authorMapper::toRepository)
        .toList();
    return this.repository.insertAll(authorPdos).totalCount().orElse(0L);
  }

  public AuthorRepository getRepository() {
    return repository;
  }

  public void setRepository(AuthorRepository repository) {
    this.repository = repository;
  }

  public AuthorMapper getAuthorMapper() {
    return authorMapper;
  }

  public void setAuthorMapper(
      AuthorMapper authorMapper) {
    this.authorMapper = authorMapper;
  }
}
