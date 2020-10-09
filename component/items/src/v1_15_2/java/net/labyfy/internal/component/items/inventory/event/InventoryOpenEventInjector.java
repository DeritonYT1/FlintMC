package net.labyfy.internal.component.items.inventory.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.items.inventory.event.InventoryOpenEvent;
import net.labyfy.component.processing.autoload.AutoLoad;

@Singleton
@AutoLoad
// TODO
public class InventoryOpenEventInjector {

  private final EventBus eventBus;
  private final InventoryOpenEvent.Factory eventFactory;

  @Inject
  public InventoryOpenEventInjector(EventBus eventBus, InventoryOpenEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
  }


}
