package net.labyfy.internal.component.items.inventory.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.event.InventoryUpdateSlotEvent;

@Implement(InventoryUpdateSlotEvent.class)
public class DefaultInventoryUpdateSlotEvent extends DefaultInventorySlotEvent implements InventoryUpdateSlotEvent {

  private final ItemStack previousItem;
  private final ItemStack newItem;

  @AssistedInject
  public DefaultInventoryUpdateSlotEvent(@Assisted("inventory") Inventory inventory, @Assisted("slot") int slot,
                                         @Assisted("previousItem") ItemStack previousItem, @Assisted("newItem") ItemStack newItem) {
    super(inventory, slot);
    this.previousItem = previousItem;
    this.newItem = newItem;
  }

  @Override
  public ItemStack getPreviousItem() {
    return this.previousItem;
  }

  @Override
  public ItemStack getNewItem() {
    return this.newItem;
  }
}
