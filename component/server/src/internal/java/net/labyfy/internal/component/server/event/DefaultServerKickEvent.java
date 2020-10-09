package net.labyfy.internal.component.server.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.event.ServerKickEvent;

@Implement(ServerKickEvent.class)
public class DefaultServerKickEvent extends DefaultServerAddressEvent implements ServerKickEvent {

  private final ChatComponent reason;

  @AssistedInject
  public DefaultServerKickEvent(@Assisted("address") ServerAddress address, @Assisted("reason") ChatComponent reason) {
    super(address);
    this.reason = reason;
  }

  @Override
  public Direction getDirection() {
    return Direction.RECEIVE;
  }

  @Override
  public ChatComponent getReason() {
    return this.reason;
  }
}
