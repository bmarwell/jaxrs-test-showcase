package io.github.jaxrstesting.web.rest.api.json;

import io.github.jaxrstesting.web.rest.api.value.JsonValueWrapper;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;

public class AbstractJsonValueWrapperSerializer<T extends JsonValueWrapper<String>>
    implements JsonbSerializer<T> {

  @Override
  public void serialize(
      T stringJsonValueWrapper,
      JsonGenerator jsonGenerator,
      SerializationContext serializationContext) {
    jsonGenerator.write(stringJsonValueWrapper.value());
  }
}
