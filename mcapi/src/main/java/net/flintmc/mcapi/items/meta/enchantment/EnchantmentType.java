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

package net.flintmc.mcapi.items.meta.enchantment;

import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.items.ItemRegistry;

/**
 * Represents a unique type of an enchantment.
 */
public interface EnchantmentType {

  /**
   * Retrieves the name of this type which is being used to identify this type. The name is unique
   * per {@link ItemRegistry}.
   *
   * @return The non-null registry name
   */
  NameSpacedKey getRegistryName();

  /**
   * Retrieves the highest level of this enchantment type that is possible to get in an vanilla
   * enchanting table. Flint ignores this value when an enchantment is created.
   *
   * @return The highest level which is at least 1
   */
  int getHighestLevel();

  /**
   * Retrieves the rarity to get enchantments of this type in an enchantment table.
   *
   * @return The non-null rarity of this type
   */
  EnchantmentRarity getRarity();

  /**
   * Retrieves the display name of this enchantment type which will be displayed by the client.
   *
   * @return The non-null display name of this type
   */
  ChatComponent getDisplayName();

  /**
   * Creates a new enchantment with this type and the given level.
   *
   * @param level The level of the enchantment which has to be at least 1
   * @return The new non-null enchantment of this type
   */
  Enchantment createEnchantment(int level);

  /**
   * Factory for the {@link Builder} for {@link EnchantmentType}s.
   */
  @AssistedFactory(Builder.class)
  interface Factory {

    /**
     * Creates a new builder for an {@link EnchantmentType}.
     *
     * @return The new non-null builder
     */
    Builder newBuilder();
  }

  /**
   * Builder for the {@link EnchantmentType}.
   */
  interface Builder {

    /**
     * Sets the registry name of the result of this builder. This name is unique per {@link
     * ItemRegistry}.
     *
     * @param registryName The non-null registry name of the result type
     * @return this
     */
    Builder registryName(NameSpacedKey registryName);

    /**
     * Sets the highest enchantment level of the result of this builder.
     *
     * @param highestLevel The new highest level which has to be at least 1
     * @return this
     */
    Builder highestLevel(int highestLevel);

    Builder displayName(ChatComponent displayName);

    /**
     * Sets the rarity of the result of this builder.
     *
     * @param rarity The non-null rarity of the result type
     * @return this
     */
    Builder rarity(EnchantmentRarity rarity);

    /**
     * Builds a new {@link EnchantmentType} with the parameters specified in this builder.
     *
     * <p>The only necessary parameter to be specified is {@link #registryName(NameSpacedKey)}.
     *
     * <p>If no {@link #highestLevel(int)} has been provided, 1 will be used. If no {@link
     * #rarity(EnchantmentRarity)} has been provided, {@link EnchantmentRarity#UNCOMMON} will be
     * used.
     *
     * @return The new non-null type of enchantments
     * @throws IllegalArgumentException If the specified {@link #highestLevel(int)} is smaller than
     *                                  1
     * @throws NullPointerException     If no {@link #registryName(NameSpacedKey)} (or {@code null})
     *                                  has been provided
     * @throws NullPointerException     If {@code null} has been provided as the {@link
     *                                  #rarity(EnchantmentRarity)}
     */
    EnchantmentType build();
  }
}
