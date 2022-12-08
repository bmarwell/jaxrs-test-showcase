package io.github.jaxrstesting.services.api.query.expressions;

public interface Visitable {

  void accept(AuthorQueryExpressionVisitor visitor);
}
