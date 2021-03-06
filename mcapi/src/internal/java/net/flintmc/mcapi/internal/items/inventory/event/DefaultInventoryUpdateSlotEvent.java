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

package net.flintmc.mcapi.internal.items.inventory.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.event.InventoryUpdateSlotEvent;

/**
 * {@inheritDoc}
 */
@Implement(InventoryUpdateSlotEvent.class)
public class DefaultInventoryUpdateSlotEvent extends DefaultInventorySlotEvent
    implements InventoryUpdateSlotEvent {

  private final ItemStack newItem;

  @AssistedInject
  public DefaultInventoryUpdateSlotEvent(
      @Assisted("inventory") Inventory inventory,
      @Assisted("slot") int slot,
      @Assisted("newItem") ItemStack newItem) {
    super(inventory, slot);
    this.newItem = newItem;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getNewItem() {
    return this.newItem;
  }
}
