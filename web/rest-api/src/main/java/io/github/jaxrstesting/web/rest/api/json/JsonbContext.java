package io.github.jaxrstesting.web.rest.api.json;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonbContext implements AutoCloseable {

  private static final Logger LOG = Logger.getLogger(JsonbContext.class.getName());

  private final Jsonb jsonb;

  public JsonbContext() {
    final JsonbConfig jsonbConfig =
        new JsonbConfig().withSerializers(new AuthorIdSerializer()).withNullValues(Boolean.TRUE);

    this.jsonb = JsonbBuilder.newBuilder().withConfig(jsonbConfig).build();
    LOG.log(
        Level.FINE,
        () ->
            "Using JSON-B implementation: [%s]."
                .formatted(this.jsonb.getClass().getCanonicalName()));
  }

  public Jsonb getJsonb() {
    return jsonb;
  }

  @Override
  public void close() throws Exception {
    if (this.jsonb != null) {
      try {
        this.jsonb.close();
      } catch (RuntimeException closeEx) {
        // pass
      }
    }
  }
}
