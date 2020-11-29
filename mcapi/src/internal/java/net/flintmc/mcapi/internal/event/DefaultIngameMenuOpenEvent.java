package net.flintmc.mcapi.internal.event;

import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.event.IngameMenuOpenEvent;

@Implement(IngameMenuOpenEvent.class)
public class DefaultIngameMenuOpenEvent implements IngameMenuOpenEvent {

  private boolean cancelled;

  @AssistedInject
  public DefaultIngameMenuOpenEvent() {}

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
