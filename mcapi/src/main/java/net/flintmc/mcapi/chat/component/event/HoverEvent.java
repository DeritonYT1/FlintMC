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

package net.flintmc.mcapi.chat.component.event;

import java.util.UUID;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.component.event.content.HoverEntity;
import net.flintmc.mcapi.chat.component.event.content.HoverText;

/**
 * The implementation of a hover event for chat components that will be displayed when the player
 * hovers over the component. Before 1.16, the HoverEvent may only contain one {@link HoverContent},
 * multiple components will end in an exception.
 *
 * <p>HoverEvents for the chat are available since Minecraft 1.7.10. With Minecraft 1.12.2, the
 * {@link Action#SHOW_ACHIEVEMENT} has been removed.
 */
public class HoverEvent {

  private final HoverContent[] contents;

  private HoverEvent(HoverContent... contents) {
    if (contents.length != 0) {
      Action action = contents[0].getAction();
      for (HoverContent content : contents) {
        if (action != content.getAction()) {
          throw new IllegalArgumentException("The action of every content needs to be the same");
        }
      }
    }
    this.contents = contents;
  }

  /**
   * Creates a new hover event with the given action and value.
   *
   * <p>Before Minecraft 1.16, only one {@link HoverContent} per {@link HoverEvent} is allowed.
   *
   * <p>Available since Minecraft 1.7.10.
   *
   * @param contents The non-null contents of the hover event
   * @return The new non-null hover event
   * @throws IllegalArgumentException if the actions are not the same in every component
   * @see Action
   */
  public static HoverEvent of(HoverContent... contents) {
    return new HoverEvent(contents);
  }

  /**
   * Creates a new hover event which will display a simple text on hover.
   *
   * <p>Available since Minecraft 1.7.10.
   *
   * @param texts The non-null texts to be displayed
   * @return The new non-null hover event
   */
  public static HoverEvent text(ChatComponent... texts) {
    HoverContent[] contents = new HoverContent[texts.length];
    for (int i = 0; i < texts.length; i++) {
      contents[i] = new HoverText(texts[i]);
    }
    return of(contents);
  }

  /**
   * Creates a new hover event which will display information about the given entity.
   *
   * <p>Available since Minecraft 1.7.10.
   *
   * @param entityId The non-null id of the entity
   * @param type     The non-null type of the entity to be displayed (e.g. `minecraft:zombie`)
   * @return The new non-null hover event
   */
  public static HoverEvent entity(UUID entityId, String type) {
    return entity(entityId, type, null);
  }

  /**
   * Creates a new hover event which will display information about the given entity.
   *
   * <p>Available since Minecraft 1.7.10.
   *
   * @param entityId    The non-null id of the entity
   * @param type        The non-null type of the entity to be displayed (e.g. `minecraft:zombie`)
   * @param displayName The nullable display name of the entity
   * @return The new non-null hover event
   */
  public static HoverEvent entity(UUID entityId, String type, ChatComponent displayName) {
    return of(new HoverEntity(entityId, type, displayName));
  }

  /**
   * Retrieves the non-null value of this hover event.
   */
  public HoverContent[] getContents() {
    return this.contents;
  }

  /**
   * Available actions for the {@link HoverEvent}.
   */
  public enum Action {

    /**
     * Displays the given component directly with no modifications.
     *
     * <p>Available since Minecraft 1.7.10.
     */
    SHOW_TEXT,

    /**
     * Parses the text of the given value as a json and parses an ItemStack out of it which will
     * then be displayed.
     *
     * <p>Available since Minecraft 1.7.10.
     */
    SHOW_ITEM,

    /**
     * Parses the text of the given value as a json and parses an Entity out of it which will then
     * be displayed.
     *
     * <p>Available since Minecraft 1.7.10.
     */
    SHOW_ENTITY,

    /**
     * Gets the achievement by using the text of the given value as the id for the achievement and
     * displays it. This can also display statistics.
     *
     * <p>Available since Minecraft 1.7.10 until Minecraft 1.12.2. Since 1.13, Minecraft uses
     * {@link #SHOW_TEXT} to display advancements.
     */
    @Deprecated
    SHOW_ACHIEVEMENT;

    private final String lowerName;

    Action() {
      this.lowerName = super.name().toLowerCase();
    }

    public String getLowerName() {
      return this.lowerName;
    }
  }
}
