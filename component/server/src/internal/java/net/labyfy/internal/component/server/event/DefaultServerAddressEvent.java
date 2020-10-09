package net.labyfy.internal.component.server.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.event.ServerAddressEvent;

@Implement(ServerAddressEvent.class)
public class DefaultServerAddressEvent implements ServerAddressEvent {

  private final ServerAddress address;

  @AssistedInject
  public DefaultServerAddressEvent(@Assisted("address") ServerAddress address) {
    this.address = address;
  }

  @Override
  public ServerAddress getAddress() {
    return this.address;
  }

  @Override
  public String getIP() {
    return this.getAddress().getIP();
  }

  @Override
  public int getPort() {
    return this.getAddress().getPort();
  }
}
