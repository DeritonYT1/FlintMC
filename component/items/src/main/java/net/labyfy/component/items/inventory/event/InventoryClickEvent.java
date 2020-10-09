package net.labyfy.component.items.inventory.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.eventbus.event.Cancellable;
import net.labyfy.component.eventbus.event.filter.EventGroup;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.InventoryClick;
import net.labyfy.component.items.inventory.InventoryController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This event will be fired whenever the player clicks into the inventory. It will also be fired by {@link
 * InventoryController#performClick(InventoryClick, int)} and in both the PRE and POST phases, but cancellation will
 * only have an effect in the PRE phase. The {@link ClickFilter} and {@link SlotFilter} can be applied to methods
 * subscribed to this event.
 *
 * @see Subscribe
 */
public interface InventoryClickEvent extends InventoryEvent, InventorySlotEvent, Cancellable {

  /**
   * Retrieves the item that has been clicked by the player.
   * <p>
   * In the POST phase, this will be the item after the action, so if the type was {@link InventoryClick#DROP_ALL}, it
   * would be an air stack.
   * <p>
   * For example for {@link InventoryClick#PICKUP_ALL} it would be the item that has been picked up in the PRE phase and
   * air in the POST phase or if an item has been placed the item in the POST phase and air in the PRE phase.
   *
   * @return The clicked item or {@code null} if no slot has been clicked (e.g. outside of the inventory when {@link
   * #getClickType()} == {@link InventoryClick#DROP}
   */
  ItemStack getClickedItem();

  /**
   * Retrieves the type of click that has been performed by the client.
   *
   * @return The non-null type of click
   */
  InventoryClick getClickType();

  /**
   * The {@link EventGroup} annotation to filter {@link InventoryClickEvent}s by their click type.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @EventGroup(groupEvent = InventoryClickEvent.class)
  @interface ClickFilter {

    /**
     * Retrieves the type of click to match the type in the {@link InventoryClickEvent}.
     *
     * @return The non-null type
     * @see InventoryClickEvent#getClickType()
     */
    InventoryClick value();

  }

  /**
   * Factory for the {@link InventoryClickEvent}.
   */
  @AssistedFactory(InventoryClickEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryClickEvent} with the given values.
     *
     * @param inventory The non-null inventory where the click has happened
     * @param slot      The slot in the inventory where the click has happened or {@code -1} if no slot has been clicked
     *                  (e.g. outside of the inventory when the clickType is {@link InventoryClick#DROP})
     * @param clickType The non-null type of click performed by the player
     * @return The new non-null event
     */
    InventoryClickEvent create(@Assisted("inventory") Inventory inventory, @Assisted("slot") int slot,
                               @Assisted("clickType") InventoryClick clickType);

  }

}
