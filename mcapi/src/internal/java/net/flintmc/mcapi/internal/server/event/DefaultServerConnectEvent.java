package net.flintmc.mcapi.internal.server.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.event.ServerConnectEvent;

import javax.annotation.Nullable;

@Implement(ServerConnectEvent.class)
public class DefaultServerConnectEvent extends DefaultServerAddressEvent
    implements ServerConnectEvent {

  @AssistedInject
  public DefaultServerConnectEvent(@Assisted("address") @Nullable ServerAddress address) {
    super(address);
  }
}