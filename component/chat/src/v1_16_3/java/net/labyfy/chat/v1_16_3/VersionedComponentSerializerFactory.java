package net.labyfy.chat.v1_16_3;

import com.google.inject.Inject;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.chat.serializer.DefaultComponentSerializerFactory;
import net.labyfy.component.inject.implement.Implement;
import org.apache.logging.log4j.Logger;

/**
 * 1.16.3 implementation of the labyfy {@link net.labyfy.chat.serializer.ComponentSerializer.Factory}
 */
@Implement(value = ComponentSerializer.Factory.class, version = "1.16.3")
public class VersionedComponentSerializerFactory extends DefaultComponentSerializerFactory {

  @Inject
  public VersionedComponentSerializerFactory(Logger logger) {
    super(logger, false); // The legacy hover is disabled for every version above 1.16
  }
}
