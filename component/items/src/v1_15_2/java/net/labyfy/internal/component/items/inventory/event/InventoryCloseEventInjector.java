package net.labyfy.internal.component.items.inventory.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.event.DirectionalEvent.Direction;
import net.labyfy.component.event.DirectionalEvent.DirectionFilter;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.InventoryController;
import net.labyfy.component.items.inventory.event.InventoryCloseEvent;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.server.event.PacketEvent;
import net.labyfy.component.server.event.PacketEvent.PacketFilter;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.internal.component.items.inventory.event.shadow.AccessibleSCloseWindowPacket;

@Singleton
@AutoLoad
// TODO: not being called
public class InventoryCloseEventInjector {

  private final EventBus eventBus;
  private final InventoryCloseEvent.Factory eventFactory;
  private final InventoryController controller;

  @Inject
  public InventoryCloseEventInjector(EventBus eventBus, InventoryCloseEvent.Factory eventFactory, InventoryController controller) {
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
    this.controller = controller;
  }

  @Subscribe(phase = Subscribe.Phase.ANY)
  @DirectionFilter(Direction.RECEIVE)
  @PacketFilter(packetClass = AccessibleSCloseWindowPacket.class)
  public void handleIncomingClose(PacketEvent event, Subscribe.Phase phase) {
    AccessibleSCloseWindowPacket packet = (AccessibleSCloseWindowPacket) event.getPacket();
    Inventory inventory = this.controller.getOpenInventory();
    if (inventory == null || inventory.getWindowId() != packet.getWindowId()) {
      return;
    }

    this.eventBus.fireEvent(this.eventFactory.create(inventory, Direction.RECEIVE), phase);
  }

  @Hook(
      className = "net.minecraft.client.entity.player.ClientPlayerEntity",
      methodName = "closeScreen"
  )
  public boolean closeScreen(Hook.ExecutionTime executionTime) {
    Inventory inventory = this.controller.getOpenInventory();
    if (inventory == null) {
      inventory = this.controller.getPlayerInventory();
    }

    return this.eventBus.fireEvent(this.eventFactory.create(inventory, Direction.SEND), executionTime).isCancelled();
  }

}
