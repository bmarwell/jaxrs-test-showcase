package io.github.jaxrstesting.services.api.query;

import io.github.jaxrstesting.services.api.query.expressions.QueryExpression;

public record ComplexAuthorQuery(QueryExpression queryExpression) implements AuthorQuery {
}
