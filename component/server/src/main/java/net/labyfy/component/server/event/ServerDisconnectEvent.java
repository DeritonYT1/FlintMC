package net.labyfy.component.server.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.event.DirectionalEvent;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.server.ServerAddress;

/**
 * This event will be fired whenever the user disconnects from a server. It doesn't matter whether the disconnection has
 * been called through the {@link net.labyfy.component.server.ServerController} or by the user. Note that this event
 * will NOT be fired, when the server kicks the player. It will only be fired in the POST phase.
 * <p>
 * The {@link ServerAddressFilter} can be applied to methods subscribed to this event.
 * <p>
 * In this case, the directions in the {@link DirectionalEvent} mean whether the disconnect has been initiated by the
 * server ({@link DirectionalEvent.Direction#RECEIVE}), or initiated by the client ({@link
 * DirectionalEvent.Direction#SEND}.
 *
 * @see Subscribe
 */
public interface ServerDisconnectEvent extends ServerAddressEvent, DirectionalEvent {

  /**
   * Factory for the {@link ServerDisconnectEvent}.
   */
  @AssistedFactory(ServerDisconnectEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerDisconnectEvent} with the given address.
     *
     * @param address The non-null address for the new event
     * @return The new event
     */
    ServerDisconnectEvent create(@Assisted("address") ServerAddress address);

  }


}
