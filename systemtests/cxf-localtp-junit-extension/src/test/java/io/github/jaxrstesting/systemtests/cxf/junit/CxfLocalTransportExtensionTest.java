package io.github.jaxrstesting.systemtests.cxf.junit;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.jaxrstesting.systemtests.cxf.junit.CxfLocalTransportExtension.NoneInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.AbstractMap.SimpleEntry;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CxfLocalTransportExtensionTest {

  @Test
  void resources_can_be_added() {
    final CxfLocalTransportExtension cxfExt = new CxfLocalTransportExtension()
        .withResource(CxfLocalTransportExtensionTest.class);

    assertThat(cxfExt.getResources())
        .isNotEmpty()
        .containsExactly(new SimpleEntry<>(CxfLocalTransportExtensionTest.class, new NoneInstance()));
  }

  @Test
  void resources_can_be_constructed() {
    CxfLocalTransportExtension cxfExt = new CxfLocalTransportExtension()
        .withResource(TestResource.class);;

    try {
      cxfExt.beforeAll(null);
      cxfExt.afterAll(null);
    } finally {
      shutDownInstance(cxfExt);
    }
  }

  @Test
  void providers_can_be_added() {
    final CxfLocalTransportExtension cxfExt = new CxfLocalTransportExtension()
        .withResource(TestResource.class, new TestResource())
        .withProvider(new NoneInstance());

    try {
      cxfExt.beforeAll(null);
      cxfExt.afterAll(null);
    } finally {
      shutDownInstance(cxfExt);
    }

    assertThat(cxfExt.getProviders())
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  void server_can_be_started() {
    CxfLocalTransportExtension cxfExt = null;

    try {
      cxfExt = new CxfLocalTransportExtension()
          .withResource(TestResource.class, new TestResource());
      cxfExt.beforeAll(null);
    } catch (Exception javaLangException) {
      shutDownInstance(cxfExt);
      throw new IllegalStateException(javaLangException);
    }

    // given
    final Server server = cxfExt.getServer();
    final WebClient webClient = cxfExt.getWebClient();
    final String endpointAddress = cxfExt.getEndpointAddress();

    // then
    assertThat(server)
        .isNotNull()
        .matches(Server::isStarted);

    assertThat(webClient)
        .isNotNull()
        .matches(cl -> cl.getBaseURI().toASCIIString().startsWith(endpointAddress));

    // given
    final Response response = webClient.get();

    // then
    assertThat(response)
        .extracting(Response::getStatus, rsp -> rsp.readEntity(String.class))
        .contains(200, "Hello");

    try {
      cxfExt.afterAll(null);
    } catch (Exception javaLangException) {
      shutDownInstance(cxfExt);
      throw new IllegalStateException(javaLangException);
    }
  }

  private static void shutDownInstance(CxfLocalTransportExtension cxfExt) {
    if (cxfExt != null && cxfExt.getServer() != null && cxfExt.getServer().isStarted()) {
      cxfExt.getServer().stop();
      cxfExt.getServer().destroy();
    }
  }

  @Path("")
  public static class TestResource {

    @GET
    @Path("")
    @Produces("text/plain")
    public Response sayHello() {
      return Response.ok()
          .entity("Hello")
          .build();
    }
  }
}
