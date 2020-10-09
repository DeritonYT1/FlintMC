package net.labyfy.internal.component.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.event.ServerLoginSuccessEvent;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.minecraft.client.network.login.ClientLoginNetHandler;
import net.minecraft.network.login.server.SLoginSuccessPacket;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Singleton
@AutoLoad(round = 2)
public class ServerLoginSuccessEventInjector {

  private final EventBus eventBus;
  private final ServerAddress.Factory addressFactory;
  private final ServerLoginSuccessEvent.Factory eventFactory;

  @Inject
  public ServerLoginSuccessEventInjector(EventBus eventBus, ServerAddress.Factory addressFactory, ServerLoginSuccessEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.addressFactory = addressFactory;
    this.eventFactory = eventFactory;
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      className = "net.minecraft.client.network.login.ClientLoginNetHandler",
      methodName = "handleLoginSuccess",
      parameters = @Type(reference = SLoginSuccessPacket.class)
  )
  public void handleLoginSuccess(@Named("instance") Object instance, Hook.ExecutionTime executionTime) {
    ClientLoginNetHandler handler = (ClientLoginNetHandler) instance;
    SocketAddress socketAddress = handler.getNetworkManager().getRemoteAddress();
    if (!(socketAddress instanceof InetSocketAddress)) {
      return;
    }

    ServerAddress address = this.addressFactory.create((InetSocketAddress) socketAddress);
    this.eventBus.fireEvent(this.eventFactory.create(address), executionTime);
  }

}
