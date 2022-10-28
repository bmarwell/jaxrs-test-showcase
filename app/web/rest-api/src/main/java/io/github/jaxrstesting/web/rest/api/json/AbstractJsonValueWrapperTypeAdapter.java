package io.github.jaxrstesting.web.rest.api.json;

import io.github.jaxrstesting.common.value.ValueWrapper;
import jakarta.json.bind.adapter.JsonbAdapter;

public abstract class AbstractJsonValueWrapperTypeAdapter<T extends ValueWrapper<String>>
    implements JsonbAdapter<T, String> {

  @Override
  public String adaptToJson(T stringJsonValueWrapper) {
    return stringJsonValueWrapper.value();
  }
}
