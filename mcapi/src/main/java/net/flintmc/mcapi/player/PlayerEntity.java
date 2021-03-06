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

package net.flintmc.mcapi.player;

import java.util.UUID;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.EntityNotLoadedException;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.item.ItemEntity;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.type.CooldownTracking;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.player.type.model.PlayerClothing;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.player.type.sound.SoundCategory;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.tileentity.SignTileEntity;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;

public interface PlayerEntity extends LivingEntity, CooldownTracking {

  /**
   * Whether block action restricted.
   *
   * @param world    The world to be checked.
   * @param position The block position to be checked.
   * @param mode     The game mode for the block action restricted
   * @return {@code true} if the block action restricted, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean blockActionRestricted(World world, BlockPosition position, GameMode mode);

  /**
   * Whether secondary use is active.
   *
   * @return {@code true} if the secondary use is active, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isSecondaryUseActive();

  /**
   * Plays a sound at the position of this player.
   *
   * @param sound    The sound to be played.
   * @param category The category for this sound.
   * @param volume   The volume of this sound.
   * @param pitch    The pitch of this sound.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void playSound(Sound sound, SoundCategory category, float volume, float pitch);

  /**
   * Retrieves the score of this player.
   *
   * @return The score of this player.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  int getScore();

  /**
   * Sets the score of this player.
   *
   * @param score The new score.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setScore(int score);

  /**
   * Adds the score to this player.
   *
   * @param score The score to be added.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void addScore(int score);

  /**
   * Whether the selected item can be dropped.
   *
   * @param dropEntireStack Whether the entries stack can eb dropped.
   * @return {@code true} if the selected item can be dropped, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean drop(boolean dropEntireStack);

  /**
   * Retrieves the dropped item as an entity.
   *
   * @param itemStack The dropped item.
   * @param traceItem {@code true} if the item can be traced, otherwise {@code false}.
   * @return The dropped item as an entity, or {@code null}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  ItemEntity dropItem(ItemStack itemStack, boolean traceItem);

  /**
   * Retrieves the dropped item as an entity.
   *
   * @param itemStack  The dropped item.
   * @param dropAround If {@code true}, the item will be thrown in a random direction from the
   *                   entity regardless of which direction the entity is facing.
   * @param traceItem  {@code true} if the item can be traced, otherwise {@code false}.
   * @return The dropped ite mas an entity, or {@code null}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  ItemEntity dropItem(ItemStack itemStack, boolean dropAround, boolean traceItem);

  /**
   * Whether the player can attack another player.
   *
   * @param playerEntity The player to be attacked.
   * @return {@code true} if can the player be attacked, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean canAttackPlayer(PlayerEntity playerEntity);

  /**
   * Opens a sign editor.
   *
   * @param signTileEntity The sign to be edited.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void openSignEditor(SignTileEntity signTileEntity);

  /**
   * Opens a minecart command block.
   *
   * @param commandBlockLogic The minecart command block to be opened.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void openMinecartCommandBlock(Object commandBlockLogic);

  /**
   * Opens a command block.
   *
   * @param commandBlockTileEntity The command block to be opened.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void openCommandBlock(Object commandBlockTileEntity);

  /**
   * Opens a structure block.
   *
   * @param structureBlockTileEntity The structure block to be opened.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void openStructureBlock(Object structureBlockTileEntity);

  /**
   * Opens a jigsaw.
   *
   * @param jigsawTileEntity The jigsaw to be opened.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void openJigsaw(Object jigsawTileEntity);

  /**
   * Opens a horse inventory.
   *
   * @param abstractHorseEntity The horse that has an inventory.
   * @param inventory           Inventory of this horse.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void openHorseInventory(Object abstractHorseEntity, Inventory inventory);

  /**
   * Opens a merchant inventory.
   *
   * @param merchantOffers  The offers of the merchant
   * @param container       The container identifier for this merchant
   * @param levelProgress   The level progress of this merchant.<br>
   *                        <b>Note:</b><br>
   *                        1 = Novice<br> 2 = Apprentice<br> 3 = Journeyman<br> 4 = Expert<br> 5 =
   *                        Master
   * @param experience      The total experience for this villager (Always 0 for the wandering
   *                        trader)
   * @param regularVillager {@code true} if this is a regular villager, otherwise {@code false} for
   *                        the wandering trader. When {@code false}, hides the villager level and
   *                        some other GUI elements
   * @param refreshable     {@code true} for regular villagers and {@code false} for the wandering
   *                        trader. If {@code true}, the "Villagers restock up to two times per
   *                        day".
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void openMerchantContainer(
      int container,
      Object merchantOffers,
      int levelProgress,
      int experience,
      boolean regularVillager,
      boolean refreshable);

  /**
   * Opens a book.
   *
   * @param stack The item stack which should be a book.
   * @param hand  The hand of this player.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void openBook(ItemStack stack, Hand hand);

  /**
   * Attacks the target entity with the current item.
   *
   * @param entity The entity to be attacked.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void attackTargetEntityWithCurrentItem(Entity entity);

  /**
   * Disables the shield of this player.
   *
   * @param disable {@code true} if the shield should be deactivated, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void disableShield(boolean disable);

  /**
   * Spawns the sweep particles.
   *
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void spawnSweepParticles();

  /**
   * Sends the client status packet to respawn the player.
   *
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void respawnPlayer();

  /**
   * Whether the player is an user.
   *
   * @return {@code true} if the player is an user, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isUser();

  /**
   * Retrieves the game player profile of this player.
   *
   * @return The game player profile of this player.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  GameProfile getGameProfile();

  /**
   * Wakes up this player or updates all sleeping players.
   *
   * @param updateTimer           {@code true} if the sleep timer should be updated, otherwise
   *                              {@code false}.
   * @param updateSleepingPlayers {@code true} if all sleeping players should be updated, otherwise
   *                              {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void stopSleepInBed(boolean updateTimer, boolean updateSleepingPlayers);

  /**
   * Whether the player is fully asleep.
   *
   * @return {@code true} if the player is fully asleep, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isPlayerFullyAsleep();

  /**
   * Retrieves the sleep timer of this player.
   *
   * @return The sleep timer of this player.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  int getSleepTimer();

  /**
   * Sends a status message to this player.
   *
   * @param component The message for this status.
   * @param actionbar {@code true} if the status message should be displayed in the action bar,
   *                  otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void sendStatusMessage(ChatComponent component, boolean actionbar);

  /**
   * Retrieves the bed location of this player.
   *
   * @return The player's bed location.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  BlockPosition getBedLocation();

  /**
   * Adds a custom stat to this player.
   *
   * @param resourceLocation The resource location for the stat.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void addStat(ResourceLocation resourceLocation);

  /**
   * Adds a custom stat to this player.
   *
   * @param resourceLocation The resource location for the stat.
   * @param state            The stat of the stat.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void addStat(ResourceLocation resourceLocation, int state);

  /**
   * Adds movement stats to this player.
   *
   * @param x The `x` position to be added.
   * @param y The `y` position to be added.
   * @param z The `z` position to be added.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void addMovementStat(double x, double y, double z);

  /**
   * Whether can be tried to start the fall flying of this player.
   *
   * @return {@code true} if can be tried to start the fall flying, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean tryToStartFallFlying();

  /**
   * Starts the fall flying of this player.
   *
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void startFallFlying();

  /**
   * Stops the fall flying of this player.
   *
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void stopFallFlying();

  /**
   * Gives experience points to this player.
   *
   * @param experiencePoints The points to be assigned.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void giveExperiencePoints(int experiencePoints);

  /**
   * Retrieves the experience seed of this player.
   *
   * @return The experience seed.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  int getExperienceSeed();

  /**
   * Adds the experience level to this player.
   *
   * @param experienceLevel The levels to be added.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void addExperienceLevel(int experienceLevel);

  /**
   * Retrieves the experience bar cap of this player.
   *
   * @return The experience bar cap of this player.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  int getExperienceBarCap();

  /**
   * Adds the exhaustion of this player.
   *
   * @param exhaustion The exhaustion to be added.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void addExhaustion(float exhaustion);

  /**
   * Whether the player can eat.
   *
   * @param ignoreHunger Whether hunger should be ignored.
   * @return {@code true} if the player can eat, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean canEat(boolean ignoreHunger);

  /**
   * Whether the player should heal.
   *
   * @return {@code true} if the player should heal, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean shouldHeal();

  /**
   * Whether the player is allowed to edit.
   *
   * @return {@code true} if the player is allowed to edit, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isAllowEdit();

  /**
   * Sends the abilities of this player to the server.
   *
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void sendPlayerAbilities();

  /**
   * Changes the game mode of this player.
   *
   * @param gameMode The new game mode.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setGameMode(GameMode gameMode);

  /**
   * Whether the item stack was added to this main inventory.
   *
   * @param itemStack The item stack to be added.
   * @return {@code true} if was the item stack added.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean addItemStackToInventory(ItemStack itemStack);

  /**
   * Whether the player is in creative mode.
   *
   * @return {@code true} if the player is in the creative mode, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isCreative();

  /**
   * Retrieves the scoreboard of this player.
   *
   * @return The player's scoreboard.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  Scoreboard getScoreboard();

  /**
   * Retrieves the unique identifier
   *
   * @param profile The game profile of this player.
   * @return The unique identifier of the given game profile.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  UUID getUniqueId(GameProfile profile);

  /**
   * Retrieves the offline unique identifier for this player.
   *
   * @param username The username of this player.
   * @return THe offline unique identifier.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  UUID getOfflineUniqueId(String username);

  /**
   * Whether the player is wearing the given clothing.
   *
   * @param clothing The clothing that should be worn.
   * @return {@code true} if the player is wearing the clothing, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isWearing(PlayerClothing clothing);

  /**
   * Whether the player has reduced debug.
   *
   * @return {@code true} if the player has reduced debug, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean hasReducedDebug();

  /**
   * Sets the reduced debug for this player.
   *
   * @param reducedDebug {@code true} if the reduced should be enabled for this player, otherwise
   *                     {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setReducedDebug(boolean reducedDebug);

  /**
   * Sets the primary hand of this player.
   *
   * @param primaryHand The new primary hand of this player.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setPrimaryHand(Hand.Side primaryHand);

  /**
   * Retrieves the cooldown period of this player.
   *
   * @return The cooldown period of this player.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getCooldownPeriod();

  /**
   * Retrieves the cooled attack strength of this player.
   *
   * @param strength the ticks to adjust the cooled strength of the attack.
   * @return The cooled attack strength of this player.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getCooledAttackStrength(float strength);

  /**
   * Resets the cooldown of the player.
   *
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void resetCooldown();

  /**
   * Retrieves the luck of the player.
   *
   * @return The luck of the player.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getLuck();

  /**
   * Whether the player can use a command block.
   *
   * @return {@code true} if the player can use a command block, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean canUseCommandBlock();

  /**
   * Retrieves the left shoulder entity as a {@link NBTCompound}.
   *
   * @return The left shoulder entity.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  NBTCompound getLeftShoulderEntity();

  /**
   * Retrieves the right shoulder entity as a {@link NBTCompound}.
   *
   * @return The right shoulder entity.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  NBTCompound getRightShoulderEntity();

  /**
   * Adds to the player new food statistics.
   *
   * @param foodLevel The new food level for this player.
   * @param modifier  The food saturation modifier for this player.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void addFoodStatistics(int foodLevel, float modifier);

  /**
   * Retrieves the food level of this player.
   *
   * @return The player's food level.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  int getFoodLevel();

  /**
   * Changes the food level of this player.
   *
   * @param foodLevel The new food level.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setFoodLevel(int foodLevel);

  /**
   * Whether the player needs food.
   *
   * @return {@code true} if the player needs food, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean needFood();

  /**
   * Retrieves the saturation level of this player.
   *
   * @return The player's saturation level.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getSaturationLevel();

  /**
   * Changes the saturation level of this player.
   *
   * @param saturationLevel The new saturation level.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setSaturationLevel(float saturationLevel);

  /**
   * A factory class for the {@link PlayerEntity}.
   */
  @AssistedFactory(PlayerEntity.class)
  interface Factory {

    /**
     * Creates a new {@link PlayerEntity} with the given parameters.
     *
     * @param entity     The non-null Minecraft entity.
     * @param entityType The entity type.
     * @return A created {@link PlayerEntity}.
     */
    PlayerEntity create(
        @Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);
  }

  /**
   * Service interface for creating {@link PlayerEntity}.
   */
  interface Provider {

    /**
     * Creates a new {@link PlayerEntity} with the given parameter.
     *
     * @param entity The non-null Minecraft entity.
     * @return A created {@link PlayerEntity}.
     */
    PlayerEntity get(Object entity);
  }
}
