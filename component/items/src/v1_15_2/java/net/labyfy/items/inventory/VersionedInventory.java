package net.labyfy.items.inventory;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.items.ItemRegistry;
import net.labyfy.items.ItemStack;
import net.labyfy.items.mapper.MinecraftItemMapper;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.NonNullList;

import java.util.function.Supplier;

public class VersionedInventory extends DefaultInventory {

  private final MinecraftItemMapper mapper;
  private final Supplier<Container> containerSupplier;
  private final ChatComponent title;

  public VersionedInventory(ItemRegistry registry, int windowId, InventoryType type, InventoryDimension dimension,
                            MinecraftItemMapper mapper, Supplier<Container> containerSupplier, ChatComponent title) {
    super(registry, windowId, type, dimension);
    this.mapper = mapper;
    this.containerSupplier = containerSupplier;
    this.title = title;
  }

  public Container getContainer() {
    return this.containerSupplier.get();
  }

  @Override
  public ChatComponent getTitle() {
    return this.title;
  }

  @Override
  public ItemStack[] getContents() {
    NonNullList<net.minecraft.item.ItemStack> items = this.getContainer().getInventory();
    ItemStack[] contents = new ItemStack[super.getDimension().getSlotCount()];
    for (int i = 0; i < contents.length; i++) {
      contents[i] = this.mapper.fromMinecraft(items.get(i));
    }

    return contents;
  }

  @Override
  public void setContents(ItemStack[] contents) throws IllegalArgumentException {
    if (contents.length > super.getDimension().getSlotCount()) {
      throw new IllegalArgumentException(contents.length + " are too many contents for an inventory with a size of " + super.getDimension().getSlotCount());
    }

    for (int i = 0; i < contents.length; i++) {
      ItemStack stack = contents[i] == null ? this.registry.getAirType().createStack() : contents[i];

      this.getContainer().getSlot(i).putStack((net.minecraft.item.ItemStack) this.mapper.toMinecraft(stack));
    }
  }

  @Override
  public ItemStack getItem(int slot) throws IndexOutOfBoundsException {
    return this.mapper.fromMinecraft(this.getContainer().getInventory().get(slot));
  }

  @Override
  public void setItem(int slot, ItemStack item) throws IndexOutOfBoundsException {
    this.getContainer().getSlot(slot).putStack((net.minecraft.item.ItemStack) this.mapper.toMinecraft(item == null ? this.registry.getAirType().createStack() : item));
  }

}
