package net.labyfy.internal.component.items.inventory.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.InventoryClick;
import net.labyfy.component.items.inventory.InventoryController;
import net.labyfy.component.items.inventory.event.InventoryClickEvent;
import net.labyfy.component.items.inventory.event.InventoryHotkeyPressEvent;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;

@Singleton
@AutoLoad
public class InventoryClickEventInjector {

  private final EventBus eventBus;
  private final InventoryController controller;
  private final InventoryClickEvent.Factory clickEventFactory;
  private final InventoryHotkeyPressEvent.Factory hotkeyEventFactory;
  private long mergeTimeout = -1;

  @Inject
  public InventoryClickEventInjector(EventBus eventBus, InventoryController controller,
                                     InventoryClickEvent.Factory clickEventFactory,
                                     InventoryHotkeyPressEvent.Factory hotkeyEventFactory) {
    this.eventBus = eventBus;
    this.controller = controller;
    this.clickEventFactory = clickEventFactory;
    this.hotkeyEventFactory = hotkeyEventFactory;
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      className = "net.minecraft.client.multiplayer.PlayerController",
      methodName = "windowClick",
      parameters = {@Type(reference = int.class), @Type(reference = int.class), @Type(reference = int.class), @Type(reference = ClickType.class), @Type(reference = PlayerEntity.class)}
  )
  public boolean windowClick(@Named("args") Object[] args, Hook.ExecutionTime executionTime) {
    int windowId = (int) args[0];
    Inventory inventory = this.controller.getPlayerInventory().getWindowId() == windowId ?
        this.controller.getPlayerInventory() :
        this.controller.getOpenInventory().getWindowId() == windowId ? this.controller.getOpenInventory() :
            null;
    if (inventory == null) {
      return false;
    }
    int slot = (int) args[1];
    int button = (int) args[2];
    ClickType type = (ClickType) args[3];

    InventoryClick click = null;
    switch (type) {
      case THROW:
        click = button == 1 ? InventoryClick.DROP_ALL : InventoryClick.DROP;
        break;

      case CLONE:
        click = InventoryClick.CLONE;
        break;

      case PICKUP:
        click = button == 1 ? InventoryClick.PICKUP_HALF : InventoryClick.PICKUP_ALL;
        this.mergeTimeout = System.currentTimeMillis() + 250;
        break;

      case PICKUP_ALL:
        if (this.mergeTimeout != -1 && this.mergeTimeout < System.currentTimeMillis()) {
          click = InventoryClick.MERGE_ALL;
          this.mergeTimeout = -1;
        }
        break;

      case QUICK_MOVE:
        click = InventoryClick.MOVE;
        break;

      case SWAP:
        return this.performHotkeyPress(inventory, slot, button, executionTime);

      default:
        return false;
    }

    if (click == null) {
      return false;
    }

    return this.eventBus.fireEvent(this.clickEventFactory.create(inventory, slot, click), executionTime).isCancelled();
  }

  private boolean performHotkeyPress(Inventory inventory, int slot, int hotkey, Hook.ExecutionTime executionTime) {
    return this.eventBus.fireEvent(this.hotkeyEventFactory.create(inventory, slot, hotkey), executionTime).isCancelled();
  }

}
