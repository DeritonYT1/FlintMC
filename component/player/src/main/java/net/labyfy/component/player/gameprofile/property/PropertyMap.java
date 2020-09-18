package net.labyfy.component.player.gameprofile.property;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.Map;
import java.util.Set;

/**
 * Represents the properties of a game profile.
 */
public interface PropertyMap {

  /**
   * Associates the specified value with the specified key in this map
   *
   * @param key      The key with which the specified value is to be associated
   * @param property The property to be associated with the specified key
   * @return The property to which the specified key is mapped, or {@code null} if this map contains no mapping for the key
   */
  Property put(String key, Property property);

  /**
   * Removes all  of this mappings from this map. The map will be empty after this call returns
   */
  void clear();

  /**
   * Retrieves the properties of the game profile
   *
   * @return The properties
   */
  Map<String, Set<Property>> getProperties();

  /**
   * A json serializer and deserializer of {@link PropertyMap}
   */
  interface Serializer extends JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap> {

  }

  /**
   * A factory class for {@link PropertyMap}
   */
  @AssistedFactory(PropertyMap.class)
  interface Factory {

    /**
     * Creates a new {@link PropertyMap} with the given map.
     *
     * @param properties The properties for this map.
     * @return The created property map
     */
    PropertyMap create(@Assisted("properties") Map<String, Set<Property>> properties);
  }

}
