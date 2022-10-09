package io.github.jaxrstesting.common.value;

/**
 * Marker and wrapper class for single-value records.
 * 
 * @param <T> the type the class will emit/wrap.
 */
public interface ValueWrapper<T> {

  T value();
}
