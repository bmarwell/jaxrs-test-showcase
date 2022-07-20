package io.github.jaxrstesting.systemtests.jaxrs.jsonb.support;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.adapter.JsonbAdapter;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.bind.serializer.JsonbSerializer;
import java.lang.reflect.Array;
import java.util.stream.StreamSupport;

public class JsonbProducer {

  @Inject
  Instance<JsonbConfig> configs;

  @Inject
  Instance<JsonbAdapter<?, ?>> adapters;

  @Inject
  Instance<JsonbSerializer<?>> serializers;

  @Inject
  Instance<JsonbDeserializer<?>> deserializers;

  @Produces
  Jsonb jsonb() {
    JsonbConfig config = (configs.isUnsatisfied() ? new JsonbConfig() : configs.get())
        .withAdapters(adapters())
        .withSerializers(serializers())
        .withDeserializers(deserializers());
    return JsonbBuilder.create(config);
  }

  private JsonbAdapter[] adapters() {
    return toArray(JsonbAdapter.class, adapters);
  }

  private JsonbSerializer[] serializers() {
    return toArray(JsonbSerializer.class, serializers);
  }

  private JsonbDeserializer[] deserializers() {
    return toArray(JsonbDeserializer.class, deserializers);
  }

  @SuppressWarnings("unchecked")
  private <T> T[] toArray(Class<T> type, Iterable iterable) {
    return StreamSupport.stream(((Iterable<T>) iterable).spliterator(), false)
        .toArray(size -> (T[]) Array.newInstance(type, size));
  }
}
