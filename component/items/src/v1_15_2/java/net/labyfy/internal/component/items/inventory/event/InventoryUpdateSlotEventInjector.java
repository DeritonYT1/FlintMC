package net.labyfy.internal.component.items.inventory.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.event.DirectionalEvent;
import net.labyfy.component.event.DirectionalEvent.DirectionFilter;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.InventoryController;
import net.labyfy.component.items.inventory.event.InventoryUpdateSlotEvent;
import net.labyfy.component.items.mapper.MinecraftItemMapper;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.server.event.PacketEvent;
import net.labyfy.component.server.event.PacketEvent.PacketFilter;
import net.minecraft.network.play.server.SSetSlotPacket;

@Singleton
@AutoLoad
public class InventoryUpdateSlotEventInjector {

  private final EventBus eventBus;
  private final InventoryController controller;
  private final MinecraftItemMapper itemMapper;
  private final InventoryUpdateSlotEvent.Factory eventFactory;

  @Inject
  public InventoryUpdateSlotEventInjector(EventBus eventBus, InventoryController controller, MinecraftItemMapper itemMapper, InventoryUpdateSlotEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.controller = controller;
    this.itemMapper = itemMapper;
    this.eventFactory = eventFactory;
  }

  @Subscribe(phase = Subscribe.Phase.ANY)
  @DirectionFilter(DirectionalEvent.Direction.RECEIVE)
  @PacketFilter(phase = PacketEvent.ProtocolPhase.PLAY, packetClass = SSetSlotPacket.class)
  public void handleSetSlot(PacketEvent event, Subscribe.Phase phase) {
    SSetSlotPacket packet = (SSetSlotPacket) event.getPacket();

    Inventory inventory = packet.getWindowId() == this.controller.getPlayerInventory().getWindowId() ? this.controller.getPlayerInventory() :
        packet.getWindowId() == this.controller.getOpenInventory().getWindowId() ? this.controller.getOpenInventory() :
            null;
    if (inventory == null) {
      return;
    }

    int slot = packet.getSlot();
    ItemStack previousItem = inventory.getItem(slot);
    ItemStack newItem = this.itemMapper.fromMinecraft(packet.getStack());

    this.eventBus.fireEvent(this.eventFactory.create(inventory, slot, previousItem, newItem), phase);
  }

}
