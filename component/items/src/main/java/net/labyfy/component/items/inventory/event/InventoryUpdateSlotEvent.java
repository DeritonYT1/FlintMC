package net.labyfy.component.items.inventory.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.Inventory;

// TODO only called by the server? idk if the server sends the packet when the client moves an item in the inventory
// only pre phase
public interface InventoryUpdateSlotEvent extends InventorySlotEvent, InventoryEvent/*, TODO DirectionalEvent*/ {

  ItemStack getPreviousItem();

  ItemStack getNewItem();

  @AssistedFactory(InventoryUpdateSlotEvent.class)
  interface Factory {

    InventoryUpdateSlotEvent create(@Assisted("inventory") Inventory inventory, @Assisted("slot") int slot,
                                    @Assisted("previousItem") ItemStack previousItem, @Assisted("newItem") ItemStack newItem);

  }

}
