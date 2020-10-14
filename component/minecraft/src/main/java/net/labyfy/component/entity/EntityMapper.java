package net.labyfy.component.entity;

import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.component.entity.item.ItemEntity;
import net.labyfy.component.entity.mob.MobEntity;
import net.labyfy.component.entity.type.EntityPose;
import net.labyfy.component.items.inventory.EquipmentSlotType;
import net.labyfy.component.items.mapper.MinecraftItemMapper;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.player.type.GameMode;
import net.labyfy.component.player.type.hand.HandMapper;
import net.labyfy.component.player.type.sound.SoundMapper;
import net.labyfy.component.resources.ResourceLocationProvider;

/**
 * Mapper between Minecraft entity and Labyfy entity.
 */
public interface EntityMapper {

  /**
   * Creates a new {@link EquipmentSlotType} by using the given Minecraft equipment slot type as the base.
   *
   * @param handle The non-null Minecraft equipment slot type.
   * @return The new Labyfy {@link EquipmentSlotType} or {@code null} if the given equipment slot type was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft equipment slot type.
   */
  EquipmentSlotType fromMinecraftEquipmentSlotType(Object handle);

  /**
   * Creates a new Minecraft equipment slot type by using the Labyfy {@link EquipmentSlotType} as the base.
   *
   * @param equipmentSlotType The non-null Labyfy {@link EquipmentSlotType}.
   * @return The new Minecraft equipment slot type or {@code null} if the given equipment slot type was invalid.
   */
  Object toMinecraftEquipmentSlotType(EquipmentSlotType equipmentSlotType);

  /**
   * Creates a new {@link GameMode} by using the given Minecraft game type as the base.
   *
   * @param handle The non-null Minecraft game type.
   * @return The new Labyfy {@link GameMode} or {@code null} if the given game type was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft game type type.
   */
  GameMode fromMinecraftGameType(Object handle);

  /**
   * Creates a new Minecraft game type by using the Labyfy {@link GameMode} as the base.
   *
   * @param mode The non-null Labyfy {@link GameMode}.
   * @return The new Minecraft game type or {@code null} if the given game type was invalid.
   */
  Object toMinecraftGameType(GameMode mode);

  /**
   * Creates a new {@link EntityPose} by using the given Minecraft entity pose as the base.
   *
   * @param handle The non-null Minecraft entity pose.
   * @return The new Labyfy {@link EntityPose} or {@code null} if the given entity pose was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity pose.
   */
  EntityPose fromMinecraftPose(Object handle);

  /**
   * Creates a new Minecraft entity pose by using the Labyfy {@link EntityPose} as the base.
   *
   * @param pose The non-null Labyfy {@link EntityPose}.
   * @return The new Minecraft entity pose or {@code null} if the given entity pose was invalid.
   */
  Object toMinecraftPose(EntityPose pose);

  /**
   * Creates a new {@link Entity} by using the given Minecraft entity as the base.
   *
   * @param handle The non-null Minecraft entity.
   * @return The new Labyfy {@link Entity} or {@code null} if the given entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity.
   */
  Entity fromMinecraftEntity(Object handle);

  /**
   * Creates a new Minecraft entity by using the Labyfy {@link Entity} as the base.
   *
   * @param entity The non-null Labyfy {@link Entity}.
   * @return The new Minecraft entity or {@code null} if the given entity was invalid.
   */
  Object toMinecraftEntity(Entity entity);

  /**
   * Creates a new {@link Entity} by using the given Minecraft player entity as the base.
   *
   * @param handle The non-null Minecraft player entity.
   * @return The new Labyfy {@link Entity} or {@code null} if the given player entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft player entity.
   */
  PlayerEntity fromMinecraftPlayerEntity(Object handle);

  /**
   * Creates a new Minecraft player entity by using the Labyfy {@link PlayerEntity} as the base.
   *
   * @param entity The non-null Labyfy {@link PlayerEntity}.
   * @return The new Minecraft player entity or {@code null} if the given player entity was invalid.
   */
  Object toMinecraftPlayerEntity(PlayerEntity entity);

  /**
   * Creates a new {@link LivingEntity} by using the given Minecraft living entity as the base.
   *
   * @param handle The non-null Minecraft living entity.
   * @return The new Labyfy {@link LivingEntity} or {@code null} if the given living entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft living entity.
   */
  LivingEntity fromMinecraftLivingEntity(Object handle);

  /**
   * Creates a new Minecraft living entity by using the Labyfy {@link LivingEntity} as the base.
   *
   * @param entity The non-null Labyfy {@link LivingEntity}.
   * @return The new Minecraft living entity or {@code null} if the given living entity was invalid.
   */
  Object toMinecraftLivingEntity(LivingEntity entity);

  /**
   * Creates a new {@link MobEntity} by using the given Minecraft mob entity as the base.
   *
   * @param handle The non-null Minecraft living entity.
   * @return The new Labyfy {@link MobEntity} or {@code null} if the given mob entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft mob entity.
   */
  MobEntity fromMinecraftMobEntity(Object handle);

  /**
   * Creates a new Minecraft mob entity by using the Labyfy {@link MobEntity} as the base.
   *
   * @param entity The non-null Labyfy {@link MobEntity}.
   * @return The new Minecraft mob entity or {@code null} if the given mob entity was invalid.
   */
  Object toMinecraftMobEntity(MobEntity entity);

  /**
   * Creates a new {@link ItemEntity} by using the given Minecraft item entity as the base.
   *
   * @param handle The non-null Minecraft item entity.
   * @return The new Labyfy {@link ItemEntity} or {@code null} if the given item entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft item entity.
   */
  ItemEntity fromMinecraftItemEntity(Object handle);

  /**
   * Creates a new Minecraft item entity by using the Labyfy {@link ItemEntity} as the base.
   *
   * @param itemEntity The non-null Labyfy {@link ItemEntity}.
   * @return The new Minecraft item entity or {@code null} if the given item entity was invalid.
   */
  Object toMinecraftItemEntity(ItemEntity itemEntity);

  /**
   * Retrieves the hand mapper.
   *
   * @return The hand mapper.
   */
  HandMapper getHandMapper();

  /**
   * Retrieves the sound mapper.
   *
   * @return The sound mapper.
   */
  SoundMapper getSoundMapper();

  /**
   * Retrieves the component mapper.
   *
   * @return The component mapper.
   */
  MinecraftComponentMapper getComponentMapper();

  /**
   * Retrieves the item mapper.
   *
   * @return The item mapper.
   */
  MinecraftItemMapper getItemMapper();

  /**
   * Retrieves the resource location provider.
   *
   * @return The resource location provider.
   */
  ResourceLocationProvider getResourceLocationProvider();

}
