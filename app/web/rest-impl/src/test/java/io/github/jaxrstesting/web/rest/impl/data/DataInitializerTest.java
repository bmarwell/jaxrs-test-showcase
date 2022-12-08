package io.github.jaxrstesting.web.rest.impl.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.jaxrstesting.services.api.Author;
import io.github.jaxrstesting.services.api.BookstoreQueryService;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DataInitializerTest {

  @Test
  void can_read_json_authors() {
    final DataInitializer dataInitializer = new DataInitializer();
    final BookstoreQueryService service = mock(BookstoreQueryService.class);
    when(service.insertAuthors(any(List.class)))
        .thenAnswer(args -> (long) (((List<Author>) args.getArgument(0)).size()));

    // when
    final long testData = dataInitializer.importTestData(service);

    // then
    verify(service).insertAuthors(any(List.class));
    assertThat(testData)
        .isGreaterThan(0L);
  }

}
