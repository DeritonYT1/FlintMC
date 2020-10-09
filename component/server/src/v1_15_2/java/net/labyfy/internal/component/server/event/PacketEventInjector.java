package net.labyfy.internal.component.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.netty.util.concurrent.GenericFutureListener;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.server.event.PacketEvent;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;

@Singleton
@AutoLoad
// TODO not working (and all events that require the PacketEvent too)
public class PacketEventInjector {

  private final EventBus eventBus;
  private final PacketEvent.Factory eventFactory;

  @Inject
  public PacketEventInjector(EventBus eventBus, PacketEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
  }

  @Hook(
      className = "net.minecraft.network.NetworkManager",
      methodName = "processPacket",
      parameters = {@Type(reference = IPacket.class), @Type(reference = INetHandler.class)}
  )
  public void processIncomingPacket(@Named("args") Object[] args) {
    Object packet = args[0];
    System.out.println("Received: " + packet);
  }

  @Hook(
      className = "net.minecraft.network.NetworkManager",
      methodName = "dispatchPacket",
      parameters = {@Type(reference = IPacket.class), @Type(reference = GenericFutureListener.class)}
  )
  public void dispatchOutgoingPacket(@Named("args") Object[] args) {
    Object packet = args[0];
    System.out.println("Send: " + packet);
  }
}
