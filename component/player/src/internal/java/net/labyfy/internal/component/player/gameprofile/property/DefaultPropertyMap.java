package net.labyfy.internal.component.player.gameprofile.property;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.gameprofile.property.Property;
import net.labyfy.component.player.gameprofile.property.PropertyMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of {@link PropertyMap}
 */
@Implement(PropertyMap.class)
public class DefaultPropertyMap implements PropertyMap {

  private final Map<String, Set<Property>> properties;

  @AssistedInject
  private DefaultPropertyMap(
          @Assisted("properties") Map<String, Set<Property>> properties
  ) {
    this.properties = properties;
  }

  @Override
  public Map<String, Set<Property>> getProperties() {
    return this.properties;
  }

  @Override
  public Property put(String key, Property property) {
    Set<Property> properties = this.properties.get(key);

    if (properties == null) {
      properties = new HashSet<>();
    }

    properties.add(property);
    this.properties.put(key, properties);

    return property;
  }

  /**
   * Removes all  of this mappings from this map. The map will be empty after this call returns
   */
  @Override
  public void clear() {
    this.properties.clear();
  }
}
