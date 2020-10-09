package net.labyfy.internal.component.server.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.event.ServerCustomPayloadEvent;
import net.labyfy.component.stereotype.NameSpacedKey;

@Implement(ServerCustomPayloadEvent.class)
public class DefaultServerCustomPayloadEvent extends DefaultServerAddressEvent implements ServerCustomPayloadEvent {

  private final Direction direction;
  private final NameSpacedKey identifier;
  private final byte[] data;
  private boolean cancelled;

  @AssistedInject
  public DefaultServerCustomPayloadEvent(@Assisted("address") ServerAddress address, @Assisted("direction") Direction direction,
                                         @Assisted("identifier") NameSpacedKey identifier, @Assisted("data") byte[] data) {
    super(address);
    this.direction = direction;
    this.identifier = identifier;
    this.data = data;
  }

  @Override
  public Direction getDirection() {
    return this.direction;
  }

  @Override
  public NameSpacedKey getIdentifier() {
    return this.identifier;
  }

  @Override
  public byte[] getData() {
    return this.data;
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
