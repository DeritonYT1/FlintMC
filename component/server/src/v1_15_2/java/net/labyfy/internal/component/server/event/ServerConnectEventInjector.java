package net.labyfy.internal.component.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.event.ServerConnectEvent;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.internal.component.server.event.shadow.ServerConnectThread;

@Singleton
@AutoLoad(round = 2)
public class ServerConnectEventInjector {

  private final EventBus eventBus;
  private final ServerAddress.Factory addressFactory;
  private final ServerConnectEvent.Factory eventFactory;

  @Inject
  public ServerConnectEventInjector(EventBus eventBus, ServerAddress.Factory addressFactory, ServerConnectEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.addressFactory = addressFactory;
    this.eventFactory = eventFactory;
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      className = "net.minecraft.client.gui.screen.ConnectingScreen$1",
      methodName = "run"
  )
  public void handleConnect(Hook.ExecutionTime executionTime, @Named("instance") Object instance) {
    ServerConnectThread screen = (ServerConnectThread) instance;
    ServerAddress address = this.addressFactory.create(screen.getIp(), screen.getPort());
    this.eventBus.fireEvent(this.eventFactory.create(address), executionTime);
  }

}
