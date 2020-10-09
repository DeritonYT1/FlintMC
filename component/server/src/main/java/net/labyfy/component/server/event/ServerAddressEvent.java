package net.labyfy.component.server.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import net.labyfy.component.eventbus.event.filter.EventGroup;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.server.ServerAddress;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The base event for everything happening with a server. The {@link ServerAddressFilter} can be applied to methods
 * subscribed to this event.
 *
 * @see Subscribe
 */
public interface ServerAddressEvent {

  /**
   * Retrieves the address of the server where this event has happened.
   *
   * @return The non-null address of the server
   */
  ServerAddress getAddress();

  /**
   * Retrieves the ip out of the address which is necessary for the {@link ServerAddressFilter}.
   *
   * @return The non-null ip
   * @see #getAddress()
   */
  @Named("ip")
  String getIP();

  /**
   * Retrieves the port out of the address which is necessary for the {@link ServerAddressFilter}.
   *
   * @return The port
   * @see #getAddress()
   */
  @Named("port")
  int getPort();

  /**
   * The {@link EventGroup} annotation to filter {@link ServerAddressEvent}s by their ip and port.
   *
   * @see EventGroup
   */
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @EventGroup(groupEvent = ServerAddressEvent.class)
  @interface ServerAddressFilter {

    /**
     * Retrieves the ip to match the ip in the given {@link ServerAddressEvent}.
     *
     * @return The non-null ip
     * @see ServerAddressEvent#getIP()
     */
    @Named("ip") String ip();

    /**
     * Retrieves the port to match the port in the given {@link ServerAddressEvent}.
     *
     * @return The port
     * @see ServerAddressEvent#getPort()
     */
    @Named("port") int port() default 25565;

  }

  /**
   * Factory for the {@link ServerAddressEvent}.
   */
  @AssistedFactory(ServerAddressEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerAddressEvent} with the given address.
     *
     * @param address The non-null address for the new event
     * @return The new event
     */
    ServerAddressEvent create(@Assisted("address") ServerAddress address);

  }

}
