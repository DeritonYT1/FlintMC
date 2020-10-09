package net.labyfy.internal.component.server.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.event.ServerDisconnectEvent;

@Implement(ServerDisconnectEvent.class)
public class DefaultServerDisconnectEvent extends DefaultServerAddressEvent implements ServerDisconnectEvent {

  @AssistedInject
  public DefaultServerDisconnectEvent(@Assisted("address") ServerAddress address) {
    super(address);
  }

  @Override
  public Direction getDirection() {
    return Direction.SEND;
  }
}
