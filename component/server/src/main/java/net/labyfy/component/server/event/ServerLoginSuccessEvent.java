package net.labyfy.component.server.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.server.ServerAddress;

/**
 * This event will be fired when the server has sent the login success packet. It will be fired in both PRE and POST
 * phases. In this case PRE/POST means before/after the client changes the message in the GUI and switches the Protocol
 * for incoming/outgoing packets to PLAY.
 * <p>
 * The {@link ServerAddressFilter} can be applied to methods subscribed to this event.
 *
 * @see Subscribe
 */
public interface ServerLoginSuccessEvent extends ServerAddressEvent {

  @AssistedFactory(ServerLoginSuccessEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerLoginSuccessEvent} with the given address.
     *
     * @param address The non-null address for the new event
     * @return The new event
     */
    ServerLoginSuccessEvent create(@Assisted("address") ServerAddress address);

  }

}
