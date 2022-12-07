package io.github.jaxrstesting.services.api.query.expressions;

import java.util.List;

public record AndExpression(List<QueryExpression> allOf) implements QueryExpression {
  @Override
  public void accept(AuthorQueryExpressionVisitor visitor) {
    visitor.visit(this);
  }
}
