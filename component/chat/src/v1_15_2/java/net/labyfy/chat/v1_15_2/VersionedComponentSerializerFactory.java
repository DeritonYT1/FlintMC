package net.labyfy.chat.v1_15_2;

import com.google.inject.Inject;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.chat.serializer.DefaultComponentSerializerFactory;
import net.labyfy.component.inject.implement.Implement;
import org.apache.logging.log4j.Logger;

@Implement(value = ComponentSerializer.Factory.class, version = "1.15.2")
public class VersionedComponentSerializerFactory extends DefaultComponentSerializerFactory {

  @Inject
  public VersionedComponentSerializerFactory(Logger logger) {
    super(logger, true); // the legacy hover is enabled for every version below 1.16
  }

}