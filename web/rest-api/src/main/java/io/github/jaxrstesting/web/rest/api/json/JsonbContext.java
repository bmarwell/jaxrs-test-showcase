package io.github.jaxrstesting.web.rest.api.json;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

public class JsonbContext implements AutoCloseable {

  private final Jsonb jsonb;

  public JsonbContext() {
    final JsonbConfig jsonbConfig =
        new JsonbConfig().withSerializers(new AuthorIdSerializer()).withNullValues(Boolean.TRUE);

    this.jsonb = JsonbBuilder.newBuilder().withConfig(jsonbConfig).build();
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
