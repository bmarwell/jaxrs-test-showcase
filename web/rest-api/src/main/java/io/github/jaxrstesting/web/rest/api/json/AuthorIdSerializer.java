package io.github.jaxrstesting.web.rest.api.json;

import io.github.jaxrstesting.web.rest.api.value.AuthorId;
import jakarta.json.bind.serializer.JsonbSerializer;

public class AuthorIdSerializer extends AbstractJsonValueWrapperSerializer<AuthorId>
    implements JsonbSerializer<AuthorId> {}
