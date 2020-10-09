package net.labyfy.internal.component.items.inventory.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.event.InventoryEvent;

@Implement(InventoryEvent.class)
public class DefaultInventoryEvent implements InventoryEvent {

  private final Inventory inventory;

  @AssistedInject
  public DefaultInventoryEvent(@Assisted("inventory") Inventory inventory) {
    this.inventory = inventory;
  }

  @Override
  public Inventory getInventory() {
    return this.inventory;
  }
}
