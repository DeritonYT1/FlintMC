package net.labyfy.internal.component.items.inventory.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.event.InventorySlotEvent;

@Implement(InventorySlotEvent.class)
public class DefaultInventorySlotEvent extends DefaultInventoryEvent implements InventorySlotEvent {

  private final int slot;

  @AssistedInject
  public DefaultInventorySlotEvent(@Assisted("inventory") Inventory inventory, @Assisted("slot") int slot) {
    super(inventory);
    this.slot = slot;
  }

  @Override
  public int getSlot() {
    return this.slot;
  }
}
