package net.labyfy.component.items.inventory.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import net.labyfy.component.eventbus.event.filter.EventGroup;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.items.inventory.Inventory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The base event for everything that happens inside an inventory on a specified slot. The {@link SlotFilter} can be
 * applied to methods subscribed to this event.
 *
 * @see Subscribe
 */
public interface InventorySlotEvent extends InventoryEvent {

  /**
   * Retrieves the slot where this event has happened or {@code -1} if it didn't happen on any slot (e.g. outside of the
   * Inventory)
   * <p>
   * If the slot isn't {@code -1}, {@link Inventory#getItem(int)} will work with the given inventory from {@link
   * #getInventory()} and this slot.
   *
   * @return The slot of this event
   */
  @Named("slot")
  int getSlot();

  /**
   * The {@link EventGroup} annotation to filter {@link InventorySlotEvent}s by their slot.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @EventGroup(groupEvent = InventorySlotEvent.class)
  @interface SlotFilter {

    /**
     * Retrieves the slot to match the slot in the {@link InventorySlotEvent}.
     *
     * @return The slot
     * @see InventorySlotEvent#getSlot()
     */
    @Named("slot")
    int value();

  }

  /**
   * Factory for the {@link InventorySlotEvent}.
   */
  @AssistedFactory(InventorySlotEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventorySlotEvent} with the given inventory and slot.
     *
     * @param inventory The non-null inventory where the event has happened
     * @param slot      The slot where this event has happened or {@code -1} if it happened outside of any slot. If the
     *                  slot isn't {@code -1}, {@link Inventory#getItem(int)} with the given inventory and slot has to
     *                  work.
     * @return The new event
     */
    InventorySlotEvent create(@Assisted("inventory") Inventory inventory, @Assisted("slot") int slot);

  }

}
