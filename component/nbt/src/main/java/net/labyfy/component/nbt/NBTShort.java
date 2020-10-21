package net.labyfy.component.nbt;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

/**
 * A singed integral type.
 */
public interface NBTShort extends NBT {

  /**
   * Retrieves the short of the named binary tag.
   *
   * @return The short of the named binary tag.
   */
  short asShort();

  /**
   * A factory class for the {@link NBTShort}.
   */
  @AssistedFactory(NBTShort.class)
  interface Factory {

    /**
     * Creates a new {@link NBTShort} with the given value.
     *
     * @param value The value for the named binary tag.
     * @return A created short named binary tag.
     */
    NBTShort create(@Assisted("value") short value);

  }
}
