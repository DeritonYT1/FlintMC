package net.flintmc.mcapi.internal.items.inventory.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.event.InventoryUpdateSlotEvent;

@Implement(InventoryUpdateSlotEvent.class)
public class DefaultInventoryUpdateSlotEvent extends DefaultInventorySlotEvent
    implements InventoryUpdateSlotEvent {

  private final ItemStack previousItem;
  private final ItemStack newItem;

  @AssistedInject
  public DefaultInventoryUpdateSlotEvent(
      @Assisted("inventory") Inventory inventory,
      @Assisted("slot") int slot,
      @Assisted("previousItem") ItemStack previousItem,
      @Assisted("newItem") ItemStack newItem) {
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
