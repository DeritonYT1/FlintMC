package net.labyfy.component.server.event;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import net.labyfy.component.event.DirectionalEvent;
import net.labyfy.component.eventbus.event.Cancellable;
import net.labyfy.component.eventbus.event.filter.EventGroup;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface PacketEvent extends DirectionalEvent, Cancellable {

  @Named("packet")
  Object getPacket();

  ProtocolPhase getPhase();

  enum ProtocolPhase {

    HANDSHAKE,
    LOGIN,
    PLAY,
    STATUS,
    ANY

  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @EventGroup(groupEvent = PacketEvent.class)
  @interface PacketFilter {

    ProtocolPhase phase() default ProtocolPhase.ANY;

    @Named("packet")
    Class<?> packetClass() default Object.class;

  }

  @AssistedFactory(PacketEvent.class)
  interface Factory {

    PacketEvent create(@Assisted("packet") Object packet, @Assisted("phase") ProtocolPhase phase,
                       @Assisted("direction") Direction direction);

  }

}
