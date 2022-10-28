package io.github.jaxrstesting.web.rest.api.json;

import io.github.jaxrstesting.common.value.AuthorId;
import jakarta.json.bind.adapter.JsonbAdapter;

public class AuthorIdAdapter extends AbstractJsonValueWrapperTypeAdapter<AuthorId>
    implements JsonbAdapter<AuthorId, String> {

  @Override
  public AuthorId adaptFromJson(String value) {
    return AuthorId.fromString(value);
  }
}
