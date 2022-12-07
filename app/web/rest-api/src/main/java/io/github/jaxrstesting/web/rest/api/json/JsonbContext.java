package io.github.jaxrstesting.web.rest.api.json;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.config.PropertyNamingStrategy;
import jakarta.json.bind.config.PropertyVisibilityStrategy;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class JsonbContext implements AutoCloseable, ContextResolver<Jsonb> {

  private static final Logger LOG = Logger.getLogger(JsonbContext.class.getName());

  private final Jsonb jsonb;

  public JsonbContext() {
    final JsonbConfig jsonbConfig =
        new JsonbConfig()
            .withAdapters(new AuthorIdAdapter())
            .withNullValues(Boolean.TRUE)
            .withFormatting(Boolean.TRUE)
            .withPropertyVisibilityStrategy(new PrivateVisibilityStrategy())
            .withPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES);

    this.jsonb = JsonbBuilder.newBuilder()
        .withConfig(jsonbConfig)
        .build();

    LOG.log(
        Level.INFO,
        () -> "Using JSON-B implementation: [%s].".formatted(this.jsonb.getClass().getCanonicalName()));
  }

  public Jsonb getJsonb() {
    return jsonb;
  }

  @Override
  public void close() {
    if (this.jsonb != null) {
      try {
        this.jsonb.close();
      } catch (Exception closeEx) {
        // pass
      }
    }
  }

  @Override
  public Jsonb getContext(Class<?> type) {
    return this.getJsonb();
  }

  static class PrivateVisibilityStrategy implements PropertyVisibilityStrategy {

    @Override
    public boolean isVisible(Field field) {
      return false;
    }

    @Override
    public boolean isVisible(Method method) {
      return true;
    }
  }
}
