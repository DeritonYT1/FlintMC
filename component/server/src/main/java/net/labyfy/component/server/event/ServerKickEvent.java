package net.labyfy.component.server.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.server.ServerAddress;

/**
 * This event will be fired whenever the user gets kicked from a server. Note that this event will NOT be fired if the
 * user disconnects from the server by himself. It will only be fired in the POST phase.
 * <p>
 * The {@link ServerAddressFilter} can be applied to methods subscribed to this event.
 *
 * @see Subscribe
 */
public interface ServerKickEvent extends ServerAddressEvent, ServerDisconnectEvent {

  /**
   * Retrieves the reason for the kick which has been sent by the server.
   *
   * @return The non-null reason for the kick
   */
  ChatComponent getReason();

  /**
   * Factory for the {@link ServerKickEvent}.
   */
  @AssistedFactory(ServerKickEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerKickEvent} with the given address and reason.
     *
     * @param address The non-null address for the new event
     * @param reason  The non-null reason for the kick
     * @return The new event
     */
    ServerKickEvent create(@Assisted("address") ServerAddress address, @Assisted("reason") ChatComponent reason);

  }

}
