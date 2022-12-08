package io.github.jaxrstesting.web.rest.impl.data;

import io.github.jaxrstesting.services.api.Author;
import io.github.jaxrstesting.services.api.BookstoreQueryService;
import io.github.jaxrstesting.web.rest.api.json.JsonbContext;
import io.github.jaxrstesting.web.rest.api.value.AuthorRestDto;
import io.github.jaxrstesting.web.rest.impl.mapper.AuthorMapper;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.json.bind.Jsonb;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

@WebListener
public class DataInitializer implements ServletContextListener {

  private final Logger logger = Logger.getLogger(DataInitializer.class.getName());

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContextListener.super.contextInitialized(sce);
    final Instance<BookstoreQueryService> select = CDI.current().select(BookstoreQueryService.class);

    importTestData(select.get());
  }

  protected long importTestData(BookstoreQueryService bookstoreQueryService) {
    final List<Author> testData = new TestData().getTestData()
        .map(new AuthorMapper()::mapToAuthor)
        .toList();
    final long insertAuthors = bookstoreQueryService.insertAuthors(testData);

    logger.log(Level.INFO, "Inserted [" + insertAuthors + "] authors.");

    return insertAuthors;
  }

  static class TestData {

    public Stream<AuthorRestDto> getTestData() {
      try (var ds = this.getClass().getResourceAsStream("testdata.json");
          var jsonbContext = new JsonbContext()) {
        final Jsonb jsonb = jsonbContext.getJsonb();
        final AuthorRestDto[] authors = jsonb.fromJson(Objects.requireNonNull(ds), AuthorRestDto[].class);
        Logger.getLogger(TestData.class.getName())
            .log(Level.INFO, "authors: " + Arrays.toString(authors));

        return Arrays.stream(authors);
      } catch (IOException javaIoIOException) {
        throw new UncheckedIOException(javaIoIOException);
      }
    }
  }
}
