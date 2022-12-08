package io.github.jaxrstesting.services.impl.inmemory;

import io.github.jaxrstesting.services.api.query.ComplexAuthorQuery;
import io.github.jaxrstesting.services.api.query.expressions.AndExpression;
import io.github.jaxrstesting.services.api.query.expressions.AuthorMatchExpression;
import io.github.jaxrstesting.services.api.query.expressions.AuthorQueryExpressionVisitor;
import io.github.jaxrstesting.services.api.query.expressions.OrExpression;
import io.github.jaxrstesting.services.api.query.expressions.QueryExpression;
import io.github.jaxrstesting.services.impl.inmemory.repository.AuthorPdoCriteria;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ComplexAuthorQueryExpressionVisitor implements AuthorQueryExpressionVisitor {

  private AuthorPdoCriteria criteria = AuthorPdoCriteria.authorPdo;

  @Override
  public void visit(ComplexAuthorQuery query) {
    query.queryExpression().accept(this);
  }

  @Override
  public void visit(AndExpression andExpression) {
    final AtomicReference<AuthorPdoCriteria> authorPdoCriteria = new AtomicReference<>(AuthorPdoCriteria.authorPdo);
    final List<QueryExpression> allOf = andExpression.allOf();

    for (QueryExpression queryExpression : allOf) {
      final ComplexAuthorQueryExpressionVisitor visitor = new ComplexAuthorQueryExpressionVisitor();
      queryExpression.accept(visitor);
      authorPdoCriteria.getAndUpdate(crit -> crit.and(visitor.criteria));
    }

    this.criteria = this.criteria.and(authorPdoCriteria.get());
  }

  @Override
  public void visit(OrExpression orExpression) {
    final AtomicReference<AuthorPdoCriteria> authorPdoCriteria = new AtomicReference<>(AuthorPdoCriteria.authorPdo);
    final List<QueryExpression> anyOf = orExpression.anyOf();

    for (QueryExpression queryExpression : anyOf) {
      final ComplexAuthorQueryExpressionVisitor visitor = new ComplexAuthorQueryExpressionVisitor();
      queryExpression.accept(visitor);
      authorPdoCriteria.getAndUpdate(crit -> crit.or(visitor.criteria));
    }

    this.criteria = this.criteria.and(authorPdoCriteria.get());
  }

  @Override
  public void visit(AuthorMatchExpression authorMatchExpression) {
    final AtomicReference<AuthorPdoCriteria> authorPdoCriteria = new AtomicReference<>(criteria);
    authorPdoCriteria.getAndUpdate(crit -> makeQuery(crit, authorMatchExpression));

    this.criteria = this.criteria.and(authorPdoCriteria.get());
  }

  private AuthorPdoCriteria makeQuery(AuthorPdoCriteria crit, AuthorMatchExpression authorMatchExpression) {
    final Class<? extends AuthorPdoCriteria> aClass = crit.getClass();
    try {
      final Field field = aClass.getField(authorMatchExpression.fieldName());
      final Object actualField = field.get(crit);
      final Class<?> actualFieldClass = actualField.getClass();
      final Method classMethod = actualFieldClass.getMethod("is", Object.class);
      return (AuthorPdoCriteria) classMethod.invoke(actualField, authorMatchExpression.value());
    } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException
        | InvocationTargetException javaLangNoSuchFieldException) {
      throw new RuntimeException(javaLangNoSuchFieldException);
    }

  }

  public AuthorPdoCriteria getCriteria() {
    return this.criteria;
  }
}
