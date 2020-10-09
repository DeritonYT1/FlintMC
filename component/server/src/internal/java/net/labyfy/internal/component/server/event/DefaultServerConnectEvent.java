package net.labyfy.internal.component.server.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.event.ServerConnectEvent;

@Implement(ServerConnectEvent.class)
public class DefaultServerConnectEvent extends DefaultServerAddressEvent implements ServerConnectEvent {

  @AssistedInject
  public DefaultServerConnectEvent(@Assisted("address") ServerAddress address) {
    super(address);
  }

}
