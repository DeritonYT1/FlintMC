package net.labyfy.component.server.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.event.DirectionalEvent;
import net.labyfy.component.eventbus.event.Cancellable;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.stereotype.NameSpacedKey;

/**
 * This event will be fired whenever a custom payload is being sent to (or received from) the server. It will be fired
 * in both PRE and POST phases, but cancellation only has an effect in the PRE phase.
 * <p>
 * The {@link ServerAddressFilter} can be applied to methods subscribed to this event.
 *
 * @see Subscribe
 */
public interface ServerCustomPayloadEvent extends ServerAddressEvent, DirectionalEvent, Cancellable {

  /**
   * Retrieves the identifier of this custom payload.
   *
   * @return The non-null identifier
   */
  NameSpacedKey getIdentifier();

  /**
   * Retrieves the contents of this custom payload.
   *
   * @return The non-null contents
   */
  byte[] getData();

  /**
   * Factory for the {@link ServerCustomPayloadEvent}.
   */
  @AssistedFactory(ServerCustomPayloadEvent.class)
  interface Factory {

    /**
     * Creates a new {@link ServerCustomPayloadEvent} with the given address.
     *
     * @param address The non-null address for the new event
     * @return The new event
     */
    ServerCustomPayloadEvent create(@Assisted("address") ServerAddress address, @Assisted("direction") Direction direction,
                                    @Assisted("identifier") NameSpacedKey identifier, @Assisted("data") byte[] data);

  }

}
