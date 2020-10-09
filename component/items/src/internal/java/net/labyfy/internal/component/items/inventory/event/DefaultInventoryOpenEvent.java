package net.labyfy.internal.component.items.inventory.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.event.InventoryOpenEvent;

@Implement(InventoryOpenEvent.class)
public class DefaultInventoryOpenEvent extends DefaultInventoryEvent implements InventoryOpenEvent {

  private final Direction direction;

  @AssistedInject
  public DefaultInventoryOpenEvent(@Assisted("inventory") Inventory inventory, @Assisted("direction") Direction direction) {
    super(inventory);
    this.direction = direction;
  }

  @Override
  public Direction getDirection() {
    return this.direction;
  }
}
