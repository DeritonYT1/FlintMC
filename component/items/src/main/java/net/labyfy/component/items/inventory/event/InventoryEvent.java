package net.labyfy.component.items.inventory.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.items.inventory.Inventory;

/**
 * The base event for every action that happens in an inventory.
 *
 * @see Subscribe
 */
public interface InventoryEvent {

  /**
   * Retrieves the inventory where this event has happened.
   *
   * @return The non-null inventory
   */
  Inventory getInventory();

  // TODO filter by inventory type?

  /**
   * Factory for the {@link InventoryEvent}.
   */
  @AssistedFactory(InventoryEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryEvent} with the given inventory.
     *
     * @param inventory The non-null inventory where the event has happened
     * @return The new event
     */
    InventoryEvent create(@Assisted("inventory") Inventory inventory);

  }

}
