package net.labyfy.component.items.inventory.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import net.labyfy.component.eventbus.event.Cancellable;
import net.labyfy.component.eventbus.event.filter.EventGroup;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.InventoryController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This event will be fired whenever the player clicks into the inventory. It will also be fired by {@link
 * InventoryController#performHotkeyPress(int, int)} and in both the PRE and POST phases, but cancellation will only
 * have an effect in the PRE phase. The {@link HotkeyFilter} and {@link SlotFilter} can be applied to methods subscribed
 * to this event.
 *
 * @see Subscribe
 */
public interface InventoryHotkeyPressEvent extends InventorySlotEvent, Cancellable {

  /**
   * Retrieves the hotkey which has been pressed.
   *
   * @return The hotkey in the range from 0 - 8
   */
  @Named("hotkey")
  int getHotkey();

  /**
   * Retrieves the item which has been moved from or to the slot that is bound to the hotkey in this event.
   *
   * @return The non-null item that has been moved by this event
   */
  ItemStack getClickedItem();

  /**
   * The {@link EventGroup} annotation to filter {@link InventoryHotkeyPressEvent}s by their hotkey.
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @EventGroup(groupEvent = InventoryHotkeyPressEvent.class)
  @interface HotkeyFilter {

    /**
     * Retrieves the hotkey to match the hotkey in the {@link InventoryHotkeyPressEvent}. It should be in the range from
     * 0 - 8, other values won't get any matches.
     *
     * @return The hotkey
     * @see InventoryHotkeyPressEvent#getHotkey()
     */
    @Named("hotkey")
    int value();

  }

  /**
   * Factory for the {@link InventoryHotkeyPressEvent}.
   */
  @AssistedFactory(InventoryHotkeyPressEvent.class)
  interface Factory {

    /**
     * Creates a new {@link InventoryHotkeyPressEvent} with the given values.
     *
     * @param inventory The non-null inventory where the press has happened
     * @param slot      The slot in the inventory where the press has happened (if the user performs this action, it
     *                  will be the slot where the mouse is located at when pressing the hotkey)
     * @param hotkey    The hotkey which has been pressed for this event in the range from 0 - 8
     * @return The new non-null event
     */
    InventoryHotkeyPressEvent create(@Assisted("inventory") Inventory inventory, @Assisted("slot") int slot,
                                     @Assisted("hotkey") int hotkey);

  }

}
