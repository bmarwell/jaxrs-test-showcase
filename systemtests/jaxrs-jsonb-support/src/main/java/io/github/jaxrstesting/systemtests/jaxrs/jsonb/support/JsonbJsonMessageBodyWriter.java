package io.github.jaxrstesting.systemtests.jaxrs.jsonb.support;

import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JsonbJsonMessageBodyWriter implements MessageBodyWriter<Object> {

  @Inject
  private Jsonb jsonb;

  @Override
  public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return true;
  }

  @Override
  public long getSize(Object o, Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(Object o, Class type, Type genericType, Annotation[] annotations, MediaType mediaType,
      MultivaluedMap httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
    jsonb.toJson(o, entityStream);
  }
}
