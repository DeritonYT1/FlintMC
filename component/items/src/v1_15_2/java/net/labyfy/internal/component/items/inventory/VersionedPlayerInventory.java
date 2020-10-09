package net.labyfy.internal.component.items.inventory;

import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.component.items.ItemRegistry;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.InventoryDimension;
import net.labyfy.component.items.inventory.InventoryType;
import net.labyfy.component.items.inventory.player.PlayerArmorPart;
import net.labyfy.component.items.inventory.player.PlayerHand;
import net.labyfy.component.items.inventory.player.PlayerInventory;
import net.labyfy.component.items.mapper.MinecraftItemMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class VersionedPlayerInventory extends VersionedInventory implements PlayerInventory {

  private final MinecraftItemMapper itemMapper;

  public VersionedPlayerInventory(ItemRegistry registry, InventoryType type, InventoryDimension dimension, ComponentBuilder.Factory componentFactory, MinecraftItemMapper itemMapper) {
    super(registry, 0, type, dimension, itemMapper, () -> Minecraft.getInstance().player.container, componentFactory.text().text("Player").build());
    this.itemMapper = itemMapper;
  }

  private ItemStack getItem(NonNullList<net.minecraft.item.ItemStack> inventory, int slot) {
    return this.itemMapper.fromMinecraft(inventory.get(slot));
  }

  private ItemStack[] map(List<net.minecraft.item.ItemStack> inventory) {
    ItemStack[] result = new ItemStack[inventory.size()];
    for (int i = 0; i < inventory.size(); i++) {
      result[i] = this.itemMapper.fromMinecraft(inventory.get(i));
    }
    return result;
  }

  @Override
  public ItemStack getItem(int slot) throws IndexOutOfBoundsException {
    this.validateSlot(slot);

    return this.mapper.fromMinecraft(Minecraft.getInstance().player.inventory.getStackInSlot(slot));
  }

  @Override
  public void setItem(int slot, ItemStack item) throws IndexOutOfBoundsException {
    Minecraft.getInstance().player.inventory.setInventorySlotContents(slot, super.mapToVanilla(item));
  }

  @Override
  public ItemStack getArmorPart(PlayerArmorPart part) {
    return this.getItem(Minecraft.getInstance().player.inventory.armorInventory, part.getIndex());
  }

  @Override
  public ItemStack getItemInHand(PlayerHand hand) {
    int slot = this.getHandSlot(hand);
    return slot == -1 ? super.registry.getAirType().createStack() : this.getItem(slot);
  }

  @Override
  public int getHandSlot(PlayerHand hand) {
    if (hand == PlayerHand.OFF_HAND) {
      return 40;
    }
    int currentItem = Minecraft.getInstance().player.inventory.currentItem;
    return currentItem >= 0 && currentItem <= 8 ? currentItem : -1;
  }

  @Override
  public boolean hasHand(PlayerHand hand) {
    return true;
  }

  @Override
  public ItemStack getCursor() {
    Object item = Minecraft.getInstance().player.inventory.getItemStack();
    return item == null ? super.registry.getAirType().createStack() : this.itemMapper.fromMinecraft(item);
  }

  @Override
  public void closeInventory() {
    if (Minecraft.getInstance().currentScreen instanceof ContainerScreen) {
      Minecraft.getInstance().player.closeScreen();
    }
  }

  @Override
  public ItemStack[] getContents() {
    net.minecraft.entity.player.PlayerInventory inventory = Minecraft.getInstance().player.inventory;
    List<net.minecraft.item.ItemStack> items = new ArrayList<>();
    items.addAll(inventory.mainInventory);
    items.addAll(inventory.armorInventory);
    items.addAll(inventory.offHandInventory);
    return this.map(items);
  }

  @Override
  public void setContents(ItemStack[] contents) throws IllegalArgumentException {
    super.validateContents(contents);

    for (int i = 0; i < contents.length; i++) {
      this.setItem(i, contents[i]);
    }
  }

}
