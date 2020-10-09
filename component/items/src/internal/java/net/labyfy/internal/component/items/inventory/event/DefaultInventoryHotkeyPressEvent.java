package net.labyfy.internal.component.items.inventory.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.event.InventoryHotkeyPressEvent;

@Implement(InventoryHotkeyPressEvent.class)
public class DefaultInventoryHotkeyPressEvent extends DefaultInventorySlotEvent implements InventoryHotkeyPressEvent {

  private final int hotkey;
  private final ItemStack clickedItem;
  private boolean cancelled;

  @AssistedInject
  public DefaultInventoryHotkeyPressEvent(@Assisted("inventory") Inventory inventory, @Assisted("slot") int slot,
                                          @Assisted("hotkey") int hotkey) {
    super(inventory, slot);
    this.hotkey = hotkey;
    this.clickedItem = slot < 0 /* outside of any slot */ ? null : inventory.getItem(slot); // TODO not working, always the item after the click instead of the clicked item
  }

  @Override
  public int getHotkey() {
    return this.hotkey;
  }

  @Override
  public ItemStack getClickedItem() {
    return this.clickedItem;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
