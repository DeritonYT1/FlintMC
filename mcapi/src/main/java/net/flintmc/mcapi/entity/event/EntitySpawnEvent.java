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

package net.flintmc.mcapi.entity.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;

/**
 * This event will be fired when an entity is spawned in the world the player is currently in.
 *
 * <p>When joining a world or server (Singleplayer or Multiplayer), this event will be fired for
 * every entity in the world and the player himself.
 *
 * <p>It will only be fired in the {@link Subscribe.Phase#PRE} phase.
 *
 * @see Subscribe
 */
@Subscribable(Phase.PRE)
public interface EntitySpawnEvent extends Event {

  /**
   * Retrieves the identifier of the spawned entity.
   *
   * @return The identifier of the non-null entity that has been spawned.
   */
  int getIdentifier();

  /**
   * Retrieves the entity that has been spawned in this event.
   *
   * @return The non-null entity that has been spawned
   */
  Entity getEntity();

  /**
   * Factory for the {@link EntitySpawnEvent}.
   */
  @AssistedFactory(EntitySpawnEvent.class)
  interface Factory {

    /**
     * Creates a new {@link EntitySpawnEvent} for the given entity.
     *
     * @param identifier The identifier of the non-null entity that has been spawned.
     * @param entity     The non-null entity that has been spawned.
     * @return The new non-null {@link EntitySpawnEvent}
     */
    EntitySpawnEvent create(@Assisted int identifier, @Assisted Entity entity);
  }
}
