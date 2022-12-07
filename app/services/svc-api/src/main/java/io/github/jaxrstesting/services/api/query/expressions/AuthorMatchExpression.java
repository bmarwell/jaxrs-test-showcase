package io.github.jaxrstesting.services.api.query.expressions;

public record AuthorMatchExpression(String fieldName, Object value) implements QueryExpression {

  @Override
  public void accept(AuthorQueryExpressionVisitor visitor) {
    visitor.visit(this);
  }

}
