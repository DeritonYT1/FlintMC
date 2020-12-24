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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * A singed integral type.
 */
public interface NBTInt extends NBT {

  /**
   * Retrieves the integer of the named binary tag.
   *
   * @return The integer of the named binary tag.
   */
  int asInt();

  /**
   * A factory class for the {@link NBTInt}.
   */
  @AssistedFactory(NBTInt.class)
  interface Factory {

    /**
     * Creates a new {@link NBTInt} with the given value.
     *
     * @param value The value for the named binary tag.
     * @return A created integer named binary tag.
     */
    NBTInt create(@Assisted("value") int value);
  }
}
