package net.flintmc.mcapi.nbt.array;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.nbt.NBT;

/** An array of long's payloads. */
public interface NBTLongArray extends NBT {

  /**
   * Retrieves the array of the named binary tag.
   *
   * @return The array of the named binary tag.
   */
  long[] asArray();

  /** A factory class for the {@link NBTLongArray}. */
  @AssistedFactory(NBTLongArray.class)
  interface Factory {

    /**
     * Creates a new {@link NBTLongArray} with the given array.
     *
     * @param value The array for the named binary tag.
     * @return A created long array named binary tag.
     */
    NBTLongArray create(@Assisted("value") long[] value);
  }
}