package io.github.jaxrstesting.web.rest.api.json;

import jakarta.json.bind.Jsonb;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JsonbMessageBodyWriter<T> implements MessageBodyWriter<T> {

  private static final Jsonb JSONB = new JsonbContext().getJsonb();

  public JsonbMessageBodyWriter() {
    // cdi
  }

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return true;
  }

  @Override
  public void writeTo(T objectToWrite, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
      MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws WebApplicationException {
    JSONB.toJson(objectToWrite, entityStream);
  }
}
