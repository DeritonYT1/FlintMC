package net.labyfy.internal.component.inject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.logging.LoggingProvider;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.inject.implement.Implement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.*;

/**
 * Default implementation of the {@link LoggingProvider}
 */
@Singleton
@Implement(LoggingProvider.class)
@AutoLoad(round = IMPLEMENT_SERVICE_ROUND, priority = IMPLEMENT_PRIORITY + 1)
public class DefaultLoggingProvider implements LoggingProvider {
  // Default prefix when no other prefix is available
  private static final String LABYFY_PREFIX = "Labyfy";

  // Function to map classes to prefixes
  private Function<Class<?>, String> prefixProvider = (clazz) -> null;

  // Cached loggers to save memory and speed up execution
  private final Map<Class<?>, Logger> loggerCache;

  @Inject
  private DefaultLoggingProvider() {
    // The cache needs to be concurrent
    loggerCache = new ConcurrentHashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Logger getLogger(Class<?> clazz) {
    return loggerCache.computeIfAbsent(clazz, this::createLogger);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPrefixProvider(Function<Class<?>, String> prefixProvider) {
    this.prefixProvider = prefixProvider;
  }

  /**
   * Creates a logger for the given class.
   *
   * @param clazz The class to create the logger for
   * @return The created logger
   */
  private Logger createLogger(Class<?> clazz) {
    return LogManager.getLogger(clazz, new AbstractMessageFactory() {
      /**
       * {@inheritDoc}
       */
      @Override
      public Message newMessage(Object message) {
        String prefix = prefixProvider.apply(clazz);
        if (prefix == null) {
          return new ParameterizedMessage("[" + LABYFY_PREFIX + " ]: {}", message);
        }

        return new ParameterizedMessage("[" + prefix + "]: {}", message);
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public Message newMessage(String message) {
        String prefix = prefixProvider.apply(clazz);
        if (prefix == null) {
          return new ParameterizedMessage("[" + LABYFY_PREFIX + "]: {}", message);
        }

        return new ParameterizedMessage("[" + prefix + "]: {}", message);
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public Message newMessage(String message, Object... params) {
        String prefix = prefixProvider.apply(clazz);
        if (prefix == null) {
          return new ParameterizedMessage("[" + LABYFY_PREFIX + "]: " + message, params);
        }

        return new ParameterizedMessage("[" + prefix + "]: " + message, params);
      }
    });
  }
}