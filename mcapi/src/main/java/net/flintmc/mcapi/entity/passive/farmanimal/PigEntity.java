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

package net.flintmc.mcapi.entity.passive.farmanimal;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.passive.AnimalEntity;

/**
 * Represents the Minecraft pig entity.
 */
public interface PigEntity extends AnimalEntity {

  /**
   * Whether the pig entity is saddled.
   *
   * @return {@code true} if the pig entity is saddled, otherwise {@code false}.
   */
  boolean isSaddled();

  /**
   * Changes whether the pig entity is saddled.
   *
   * @param saddled {@code true} if the pig entity should be saddled, otherwise {@code false}.
   */
  void setSaddled(boolean saddled);

  /**
   * Whether the pig entity is boosting.
   *
   * @return {@code true} if the pig entity is boosting, otherwise {@code false}.
   */
  boolean boost();

  /**
   * A factory class for the {@link PigEntity}.
   */
  @AssistedFactory(PigEntity.class)
  interface Factory {

    /**
     * Creates a new {@link PigEntity} with the given non-null Minecraft entity.
     *
     * @param entity The non-null Minecraft pig entity.
     * @return A created pig entity.
     */
    PigEntity create(@Assisted("entity") Object entity);
  }
}
