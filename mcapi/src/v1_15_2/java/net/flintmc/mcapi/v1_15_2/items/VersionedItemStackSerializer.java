package net.flintmc.mcapi.v1_15_2.items;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.items.ItemRegistry;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.ItemStackSerializer;
import net.flintmc.mcapi.items.type.ItemType;
import net.minecraft.nbt.CollectionNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.nbt.StringNBT;

@Singleton
@Implement(ItemStackSerializer.class)
public class VersionedItemStackSerializer implements ItemStackSerializer {

  private final ItemRegistry itemRegistry;

  @Inject
  public VersionedItemStackSerializer(ItemRegistry itemRegistry) {
    this.itemRegistry = itemRegistry;
  }

  @Override
  public ItemStack fromJson(JsonObject object) {
    if (!object.has("id")) {
      return null;
    }

    NameSpacedKey registryName = NameSpacedKey.parse(object.get("id").getAsString());
    int stackSize = object.has("Count") ? object.get("Count").getAsInt() : 1;

    ItemType type = this.itemRegistry.getType(registryName);
    Preconditions.checkNotNull(type, "type");

    ItemStack stack = type.createStack(stackSize);

    if (object.has("tag")) {
      JsonElement tag = object.get("tag");

      try {
        CompoundNBT nbt =
            JsonToNBT.getTagFromJson(tag.isJsonObject() ? tag.toString() : tag.getAsString());
        stack.getItemMeta(true).applyNBTFrom(nbt);
      } catch (CommandSyntaxException ignored) {
      }
    }

    return stack;
  }

  @Override
  public JsonObject toJson(ItemStack itemStack) {
    JsonObject object = new JsonObject();

    object.addProperty("id", itemStack.getType().getRegistryName().toString());
    object.addProperty("Count", itemStack.getStackSize());

    if (itemStack.hasItemMeta()) {
      CompoundNBT nbt = (CompoundNBT) itemStack.getItemMeta(false).getNBT();

      object.add("tag", this.asJson(nbt));
    }

    return object;
  }

  private JsonElement asJson(INBT nbt) {
    if (nbt instanceof CompoundNBT) {

      JsonObject object = new JsonObject();
      for (String key : ((CompoundNBT) nbt).keySet()) {
        object.add(key, this.asJson(((CompoundNBT) nbt).get(key)));
      }
      return object;

    } else if (nbt instanceof NumberNBT) {

      return new JsonPrimitive(((NumberNBT) nbt).getAsNumber());

    } else if (nbt instanceof StringNBT) {

      return new JsonPrimitive(nbt.getString());

    } else if (nbt instanceof CollectionNBT) {

      JsonArray array = new JsonArray();
      ((ListNBT) nbt).forEach(value -> array.add(this.asJson(value)));
      return array;
    }

    return JsonNull.INSTANCE;
  }
}
