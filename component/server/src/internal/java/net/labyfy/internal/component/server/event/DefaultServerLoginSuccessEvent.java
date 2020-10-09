package net.labyfy.internal.component.server.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.event.ServerLoginSuccessEvent;

@Implement(ServerLoginSuccessEvent.class)
public class DefaultServerLoginSuccessEvent extends DefaultServerAddressEvent implements ServerLoginSuccessEvent {

  @AssistedInject
  public DefaultServerLoginSuccessEvent(@Assisted("address") ServerAddress address) {
    super(address);
  }

}
