/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.nbt;

import java.util.Map;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * A list of fully formed tags, including their ID's, names, and payloads. No two tags may have the
 * same name.
 */
public interface NBTCompound extends NBT {

  /**
   * Retrieves the size of the key-value system.
   *
   * @return The key-value size.
   */
  int getSize();

  /**
   * Whether a tag exist inside this compound with the given key.
   *
   * @param key The key.
   * @return {@code true} if a tag exists inside this compound with the given key, otherwise {@code
   * false}.
   */
  boolean containsKey(String key);

  /**
   * Retrieves the tag associated to the given key, if any.
   *
   * @param key The key.
   * @return The tag associated to the given key, if any or {@code null}.
   */
  NBT get(String key);

  /**
   * Sets tag associated to the given key.
   *
   * @param key The key to associated.
   * @param tag The tag for the key.
   * @return This compound.
   */
  NBTCompound set(String key, NBT tag);

  /**
   * Retrieves the key-value system of this compound.
   *
   * @return The key-value system.
   */
  Map<String, NBT> getTags();

  /**
   * A factory class for the {@link NBTCompound}.
   */
  @AssistedFactory(NBTCompound.class)
  interface Factory {

    /**
     * Creates a new {@link NBTCompound}.
     *
     * @return A created compound named binary tag.
     */
    NBTCompound create();
  }
}
