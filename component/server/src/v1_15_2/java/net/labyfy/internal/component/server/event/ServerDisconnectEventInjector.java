package net.labyfy.internal.component.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.ServerController;
import net.labyfy.component.server.event.ServerDisconnectEvent;
import net.labyfy.component.transform.hook.Hook;

@Singleton
@AutoLoad(round = 2)
public class ServerDisconnectEventInjector {

  private final EventBus eventBus;
  private final ServerController controller;
  private final ServerDisconnectEvent.Factory eventFactory;

  @Inject
  public ServerDisconnectEventInjector(EventBus eventBus, ServerController controller, ServerDisconnectEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.controller = controller;
    this.eventFactory = eventFactory;
  }

  @Hook(
      className = "net.minecraft.client.world.ClientWorld",
      methodName = "sendQuittingDisconnectingPacket"
  )
  public void handleDisconnect(Hook.ExecutionTime executionTime) {
    if (!this.controller.isConnected()) {
      // singleplayer
      return;
    }

    ServerAddress address = this.controller.getConnectedServer().getAddress();
    this.eventBus.fireEvent(this.eventFactory.create(address), executionTime);
  }

}
