package io.github.jaxrstesting.services.api.query.expressions;

import io.github.jaxrstesting.services.api.query.ComplexAuthorQuery;

public interface AuthorQueryExpressionVisitor {

  void visit(ComplexAuthorQuery query);

  void visit(AndExpression andExpression);

  void visit(OrExpression orExpression);

  void visit(AuthorMatchExpression authorMatchExpression);
}
