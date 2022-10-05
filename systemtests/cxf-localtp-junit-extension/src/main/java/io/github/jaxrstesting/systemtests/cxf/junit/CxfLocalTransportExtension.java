package io.github.jaxrstesting.systemtests.cxf.junit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.transport.local.LocalConduit;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class CxfLocalTransportExtension implements BeforeAllCallback, AfterAllCallback {

  protected record NoneInstance() {
  };

  private final Map<Class<?>, Object> resources = new ConcurrentHashMap<>();
  private final List<Object> providersInstances = new ArrayList<>();


  private String endpointAddress = "local://books";
  private Server server;

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    startServer();
  }

  private void startServer() {
    JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
    sf.setResourceClasses(this.resources.keySet().stream().toList());

    for (Class<?> aClass : this.resources.keySet()) {
      final Object impl = this.resources.get(aClass);

      if (!(impl instanceof NoneInstance)) {
        sf.setResourceProvider(aClass, new SingletonResourceProvider(impl, true));
        continue;
      }

      final Optional<Object> optionalNew = tryInvokeNew(aClass);
      if (optionalNew.isPresent()) {
        sf.setResourceProvider(aClass, new SingletonResourceProvider(optionalNew.orElseThrow(), true));
      }
    }

    for (Object providersInstance : providersInstances) {
      sf.setProvider(providersInstance);
    }

    sf.setAddress(endpointAddress);

    server = sf.create();
  }

  private static Optional<Object> tryInvokeNew(Class<?> aClass) {
    try {
      final Constructor<?> constructor = aClass.getDeclaredConstructor();
      return Optional.of(constructor.newInstance());
    } catch (InvocationTargetException | NoSuchMethodException | InstantiationException
        | IllegalAccessException javaLangReflectInvocationTargetException) {
      // TODO: LOG
      return Optional.empty();
    }
  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    server.stop();
    server.destroy();
  }

  protected Map<Class<?>, Object> getResources() {
    return Map.copyOf(resources);
  }

  protected List<Object> getProvidersInstances() {
    return List.copyOf(providersInstances);
  }

  protected String getEndpointAddress() {
    return endpointAddress;
  }

  protected Server getServer() {
    return server;
  }

  public CxfLocalTransportExtension withResource(Class<?> provider) {
    this.resources.put(provider, new NoneInstance());
    return this;
  }

  public CxfLocalTransportExtension withResource(Class<?> provider, Object instance) {
    this.resources.put(provider, instance);
    return this;
  }

  public CxfLocalTransportExtension withProvider(Object instance) {
    this.providersInstances.add(instance);
    return this;
  }

  public WebClient getWebClient() {
    WebClient client = WebClient.create(endpointAddress);
    WebClient.getConfig(client).getRequestContext().put(LocalConduit.DIRECT_DISPATCH, Boolean.TRUE);

    return client;
  }
}
