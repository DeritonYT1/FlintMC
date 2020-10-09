package net.labyfy.internal.component.server.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.server.event.PacketEvent;

@Implement(PacketEvent.class)
public class DefaultPacketEvent implements PacketEvent {

  private final Object packet;
  private final ProtocolPhase phase;
  private final Direction direction;
  private boolean cancelled;

  @AssistedInject
  public DefaultPacketEvent(@Assisted("packet") Object packet, @Assisted("phase") ProtocolPhase phase,
                            @Assisted("direction") Direction direction) {
    this.packet = packet;
    this.phase = phase;
    this.direction = direction;
  }

  @Override
  public Object getPacket() {
    return this.packet;
  }

  @Override
  public ProtocolPhase getPhase() {
    return this.phase;
  }

  @Override
  public Direction getDirection() {
    return this.direction;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
