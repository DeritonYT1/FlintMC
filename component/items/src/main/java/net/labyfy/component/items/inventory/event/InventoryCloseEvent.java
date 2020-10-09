package net.labyfy.component.items.inventory.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.event.DirectionalEvent;
import net.labyfy.component.eventbus.event.Cancellable;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.items.inventory.Inventory;

/**
 * This event will be fired whenever the inventory opened in the client is being closed. It can either be closed by the
 * server ({@link DirectionalEvent#getDirection()} = {@link DirectionalEvent.Direction#RECEIVE}), or by the client
 * ({@link DirectionalEvent#getDirection()} = {@link DirectionalEvent.Direction#SEND}). In both cases it will be fired
 * in the PRE and POST phases, but cancellation will only have an effect in the PRE phase with the direction {@link
 * DirectionalEvent.Direction#SEND}. If it has been cancelled in this state, the inventory will not be closed and the
 * input will be ignored.
 * <p>
 * The {@link DirectionalEvent.DirectionFilter} can be applied to methods subscribed to this event.
 *
 * @see Subscribe
 */
public interface InventoryCloseEvent extends InventoryEvent, DirectionalEvent, Cancellable {

  @AssistedFactory(InventoryCloseEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryCloseEvent} with the given inventory.
     *
     * @param inventory The non-null inventory that has been closed
     * @param direction How the event has been invoked, {@link Direction#SEND} means that the client has closed the
     *                  inventory and {@link Direction#RECEIVE} that the server has closed it
     * @return The new event
     */
    InventoryCloseEvent create(@Assisted("inventory") Inventory inventory, @Assisted("direction") Direction direction);

  }

}
