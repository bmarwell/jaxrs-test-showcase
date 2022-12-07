package io.github.jaxrstesting.services.impl.inmemory;

import io.github.jaxrstesting.common.value.AuthorId;
import io.github.jaxrstesting.services.api.query.ComplexAuthorQuery;
import io.github.jaxrstesting.services.api.query.expressions.AndExpression;
import io.github.jaxrstesting.services.api.query.expressions.AuthorMatchExpression;
import io.github.jaxrstesting.services.impl.inmemory.repository.AuthorPdoCriteria;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ComplexAuthorQueryExpressionVisitorTest {

  @Test
  void testVisitorVisitsOr() {
    final var visitor = new ComplexAuthorQueryExpressionVisitor();
    var expression = new AndExpression(List.of(new AuthorMatchExpression("id", AuthorId.fromString("1"))));
    var query = new ComplexAuthorQuery(expression);

    // when
    visitor.visit(query);

    // then
    final AuthorPdoCriteria criteria = visitor.getCriteria();
    Assertions.assertThat(criteria)
        .isNotNull();
    System.out.println("Crit: " + criteria);

  }

}
