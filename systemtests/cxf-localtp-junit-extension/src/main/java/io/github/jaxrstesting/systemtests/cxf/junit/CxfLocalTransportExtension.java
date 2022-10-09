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

/**
 * Test extension to run CXF system tests without actually starting a full-featured web application
 * server. This extension will only start CXF. Injection is not available, resources or mocks must
 * be added manually.
 *
 * <p>
 * <strong>Implementation hint</strong>: This extension MUST be used at a class level (i.e. static
 * field). This is on purpose to avoid starting one CXF server per method, to speed up tests. If you
 * need different configurations for tests in the same class, use {@code @Nested} classes.
 * </p>
 *
 * <p>
 * This is mostly adapted from <a href=
 * "https://cwiki.apache.org/confluence/display/CXF20DOC/JAXRS+Testing">CXF20DOC/JAXRS+Testing</a>
 * and was converted into a Junit5 extension.
 * </p>
 *
 * <p>
 * <strong>Usage:</strong>
 * </p>
 * 
 * <pre>
 * &#64;RegisterExtension
 * static CxfLocalTransportExtension ltp = new CxfLocalTransportExtension()
 *     .withResource(AuthorResourceImpl.class, authorResource)
 *     .withProvider(new JsrJsonbProvider(jsonbContext.getJsonb()));
 * </pre>
 */
public class CxfLocalTransportExtension implements BeforeAllCallback, AfterAllCallback {

  protected record NoneInstance() {
  }

  private final Map<Class<?>, Object> resources = new ConcurrentHashMap<>();
  private final List<Object> providers = new ArrayList<>();


  private String endpointAddress = "local://books";
  private Server server;

  @Override
  public void beforeAll(ExtensionContext context) {
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

    for (Object providersInstance : providers) {
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
  public void afterAll(ExtensionContext context) {
    server.stop();
    server.destroy();
  }

  protected Map<Class<?>, Object> getResources() {
    return Map.copyOf(resources);
  }

  protected List<Object> getProviders() {
    return List.copyOf(providers);
  }

  protected String getEndpointAddress() {
    return endpointAddress;
  }

  protected Server getServer() {
    return server;
  }

  /**
   * Adds an instantiable resource. This is useful if you do not need other resources injected (e.g.
   * services/fields). The default constructor will be used to generate an instance.
   * 
   * @param resource the resource to register.
   * @return {@code this} for chaining.
   */
  public CxfLocalTransportExtension withResource(Class<?> resource) {
    this.resources.put(resource, new NoneInstance());
    return this;
  }

  /**
   * Adds a resource class with instantiation. This is useful if you need to mock services in your
   * tests.
   * 
   * @param resource the JAX-RS resource class.
   * @param instance the instance to be used.
   * @return {@code this} for chaining.
   * @param <T> the class type of resource, as well as the instance type.
   */
  public <T> CxfLocalTransportExtension withResource(Class<T> resource, T instance) {
    this.resources.put(resource, instance);
    return this;
  }

  /**
   * Adds a provider to the extension. Providers can be: JSON converters, MessageBodyWriters,
   * ExceptionMappers, etc.
   * 
   * @param provider the provider to add.
   * @return {@code this} for chaining.
   */
  public CxfLocalTransportExtension withProvider(Object provider) {
    this.providers.add(provider);
    return this;
  }

  /**
   * Returns a CXF-WebClient (not a JAX-RS one) configured to call this server using local transport.
   * 
   * @return a WebClient which can be used in test methods.
   */
  public WebClient getWebClient() {
    WebClient client = WebClient.create(endpointAddress);
    WebClient.getConfig(client).getRequestContext().put(LocalConduit.DIRECT_DISPATCH, Boolean.TRUE);

    return client;
  }
}
