package net.labyfy.internal.component.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.event.ServerKickEvent;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.text.ITextComponent;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Singleton
@AutoLoad(round = 2)
public class ServerKickEventInjector {

  private final EventBus eventBus;
  private final ServerAddress.Factory addressFactory;
  private final ServerKickEvent.Factory eventFactory;
  private final MinecraftComponentMapper componentMapper;

  @Inject
  public ServerKickEventInjector(EventBus eventBus, ServerAddress.Factory addressFactory,
                                 ServerKickEvent.Factory eventFactory, MinecraftComponentMapper componentMapper) {
    this.eventBus = eventBus;
    this.addressFactory = addressFactory;
    this.eventFactory = eventFactory;
    this.componentMapper = componentMapper;
  }

  @Hook(
      className = "net.minecraft.client.network.play.ClientPlayNetHandler",
      methodName = "onDisconnect",
      parameters = @Type(reference = ITextComponent.class)
  )
  public void test(@Named("instance") Object instance, @Named("args") Object[] args) {
    NetworkManager networkManager = ((ClientPlayNetHandler) instance).getNetworkManager();
    SocketAddress address = networkManager.getRemoteAddress();
    if (!(address instanceof InetSocketAddress)) {
      return;
    }

    ChatComponent reason = this.componentMapper.fromMinecraft(args[0]);
    ServerAddress serverAddress = this.addressFactory.create((InetSocketAddress) address);

    this.eventBus.fireEvent(this.eventFactory.create(serverAddress, reason), Subscribe.Phase.POST);
  }

}
