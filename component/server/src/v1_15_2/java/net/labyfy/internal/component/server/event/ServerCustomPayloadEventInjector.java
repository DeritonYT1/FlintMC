package net.labyfy.internal.component.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.event.DirectionalEvent;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.server.ConnectedServer;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.ServerController;
import net.labyfy.component.server.event.ServerCustomPayloadEvent;
import net.labyfy.component.stereotype.NameSpacedKey;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.internal.component.server.event.shadow.AccessibleCCustomPayloadPacket;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraft.util.ResourceLocation;

@Singleton
@AutoLoad
public class ServerCustomPayloadEventInjector {

  private final ServerController controller;
  private final EventBus eventBus;
  private final ServerCustomPayloadEvent.Factory customPayloadFactory;

  @Inject
  public ServerCustomPayloadEventInjector(ServerController controller, EventBus eventBus, ServerCustomPayloadEvent.Factory customPayloadFactory) {
    this.controller = controller;
    this.eventBus = eventBus;
    this.customPayloadFactory = customPayloadFactory;
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE},
      className = "net.minecraft.network.play.server.SCustomPayloadPlayPacket",
      methodName = "processPacket",
      parameters = @Type(reference = IClientPlayNetHandler.class)
  )
  public boolean handleCustomPayloadReceive(Hook.ExecutionTime executionTime, @Named("instance") Object instance) {
    SCustomPayloadPlayPacket packet = (SCustomPayloadPlayPacket) instance;

    ServerCustomPayloadEvent event =
        this.fireCustomPayload(DirectionalEvent.Direction.RECEIVE, packet.getChannelName(), packet.getBufferData(), executionTime);

    return event != null && event.isCancelled();
  }

  @Hook(
      //executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      className = "net.minecraft.network.play.client.CCustomPayloadPacket",
      methodName = "writePacketData",
      parameters = {@Type(reference = PacketBuffer.class)}
  )
  public void handleCustomPayloadSend(Hook.ExecutionTime executionTime, @Named("instance") Object instance) {
    AccessibleCCustomPayloadPacket packet = (AccessibleCCustomPayloadPacket) instance;
    ServerCustomPayloadEvent event =
        this.fireCustomPayload(DirectionalEvent.Direction.SEND, packet.getChannelName(), packet.getData(), executionTime);

    // TODO this has to be done in another method so that cancellation can work
  }

  private ServerCustomPayloadEvent fireCustomPayload(DirectionalEvent.Direction direction, ResourceLocation resourceLocation, PacketBuffer buffer, Hook.ExecutionTime executionTime) {
    ConnectedServer server = this.controller.getConnectedServer();
    if (server == null) {
      // singleplayer
      return null;
    }

    ServerAddress address = server.getAddress();

    NameSpacedKey identifier = this.createIdentifier(resourceLocation);
    byte[] data = this.copyBufferData(buffer);

    return this.eventBus.fireEvent(this.customPayloadFactory.create(address, direction, identifier, data), executionTime);
  }

  private NameSpacedKey createIdentifier(ResourceLocation resourceLocation) {
    return NameSpacedKey.of(resourceLocation.getNamespace(), resourceLocation.getPath());
  }

  private byte[] copyBufferData(PacketBuffer buffer) {
    buffer.markReaderIndex();
    buffer.readerIndex(0);

    byte[] data = new byte[buffer.readableBytes()];

    buffer.readBytes(data);
    buffer.resetReaderIndex();

    return data;
  }

}
