package net.labyfy.component.server.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.server.ServerAddress;

/**
 * This event will be fired whenever the user connects to a server. It doesn't matter whether the connection has been
 * called through the {@link net.labyfy.component.server.ServerController} or by the user. It will be fired in both PRE
 * and POST phases ignoring any errors that occur while connecting. The {@link ServerAddressFilter} can be applied to
 * methods subscribed to this event.
 *
 * @see Subscribe
 */
public interface ServerConnectEvent extends ServerAddressEvent {

  /**
   * Factory for the {@link ServerConnectEvent}.
   */
  @AssistedFactory(ServerConnectEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerConnectEvent} with the given address.
     *
     * @param address The non-null address for the new event
     * @return The new event
     */
    ServerConnectEvent create(@Assisted("address") ServerAddress address);

  }

}
