package net.labyfy.internal.component.items.inventory.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.InventoryClick;
import net.labyfy.component.items.inventory.event.InventoryClickEvent;

@Implement(InventoryClickEvent.class)
public class DefaultInventoryClickEvent extends DefaultInventoryEvent implements InventoryClickEvent {

  private final ItemStack clickedItem;
  private final InventoryClick clickType;
  private final int slot;
  private boolean cancelled;

  @AssistedInject
  public DefaultInventoryClickEvent(@Assisted("inventory") Inventory inventory, @Assisted("slot") int slot,
                                    @Assisted("clickType") InventoryClick clickType) {
    super(inventory);
    this.clickedItem = slot < 0 /* outside of any slot */ ? null : inventory.getItem(slot);
    this.clickType = clickType;
    this.slot = slot;
  }

  @Override
  public ItemStack getClickedItem() {
    return this.clickedItem;
  }

  @Override
  public InventoryClick getClickType() {
    return this.clickType;
  }

  @Override
  public int getSlot() {
    return this.slot;
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
