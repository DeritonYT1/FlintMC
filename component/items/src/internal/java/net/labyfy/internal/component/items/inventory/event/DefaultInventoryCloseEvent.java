package net.labyfy.internal.component.items.inventory.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.event.InventoryCloseEvent;

@Implement(InventoryCloseEvent.class)
public class DefaultInventoryCloseEvent extends DefaultInventoryEvent implements InventoryCloseEvent {

  private final Direction direction;
  private boolean cancelled;

  @AssistedInject
  public DefaultInventoryCloseEvent(@Assisted("inventory") Inventory inventory, @Assisted("direction") Direction direction) {
    super(inventory);
    this.direction = direction;
  }

  @Override
  public Direction getDirection() {
    return this.direction;
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
