package net.labyfy.internal.component.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.event.TickEvent;
import net.labyfy.component.inject.implement.Implement;

@Implement(TickEvent.class)
public class DefaultTickEvent implements TickEvent {

  private final Type type;

  @AssistedInject
  public DefaultTickEvent(@Assisted("type") Type type) {
    this.type = type;
  }

  @Override
  public Type getType() {
    return this.type;
  }
}
