package net.labyfy.component.player;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.player.PlayerInventory;
import net.labyfy.component.player.overlay.TabOverlay;
import net.labyfy.component.player.util.Hand;

import java.util.List;

/**
 * Represents the client player
 */
@Implement(Player.class)
public interface ClientPlayer extends Player {

  /**
   * Retrieves the inventory of this player.
   *
   * @return The inventory of this player
   */
  PlayerInventory getPlayerInventory();

  /**
   * Retrieves the armor contents of this player.
   *
   * @return The armor contents of this player.
   */
  // TODO: 04.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready
  List<Object> getArmorContents();

  /**
   * Retrieves the experience level of this player.
   *
   * @return The experience level of this player
   */
  int getExperienceLevel();

  /**
   * Retrieves the experience total of this player.
   *
   * @return The experience total of this player
   */
  int getExperienceTotal();

  /**
   * Retrieves the experience of this player.
   *
   * @return The experience of this player.
   */
  float getExperience();

  /**
   * Retrieves the language of this player.
   *
   * @return The language of this player
   */
  String getLocale();

  /**
   * Retrieves the tab overlay of this player.
   *
   * @return The tab overlay of this player.
   */
  TabOverlay getTabOverlay();

  /**
   * Whether the player has any information
   *
   * @return {@code true} if the player has any information, otherwise {@code false}
   */
  boolean hasPlayerInfo();

  /**
   * Retrieves the <b>FOV</b> modifier of this player.
   *
   * @return The fov modifier of this player
   */
  float getFovModifier();

  /**
   * Whether auto jump is enabled.
   *
   * @return {@code true} if auto jump is enabled, otherwise {@code false}
   */
  boolean isAutoJumpEnabled();

  /**
   * Whether the player rides a horse
   *
   * @return {@code true} if the player rides a horse, otherwise {@code false}
   */
  boolean isRidingHorse();

  /**
   * Whether the player rows a boat
   *
   * @return {@code true} if the player rows a boat, otherwise {@code false}
   */
  boolean isRowingBoat();

  /**
   * Whether the player's death screen is shown
   *
   * @return {@code true} if the player's death screen is shown, otherwise {@code false}
   */
  boolean isShowDeathScreen();

  /**
   * Retrieves the server brand of this player.
   *
   * @return The server brand of this player.
   */
  String getServerBrand();

  /**
   * Sets the server brand of this player.
   *
   * @param brand The new server brand.
   */
  void setServerBrand(String brand);

  /**
   * Retrieves the water brightness of this player.
   *
   * @return The water brightness of this player.
   */
  float getWaterBrightness();

  /**
   * Retrieves the previous time in portal of this player.
   *
   * @return The previous time in portal of this player.
   */
  float getPrevTimeInPortal();

  /**
   * Retrieves the time in portal of this player.
   *
   * @return The time in portal of this player.
   */
  float getTimeInPortal();

  /**
   * Sends a message to the chat
   *
   * @param content The message content
   */
  void sendMessage(String content);

  /**
   * Performs a command
   *
   * @param command The command to perform
   * @return {@code true} was the command successful performed, otherwise {@code false}
   */
  boolean performCommand(String command);

  /**
   * Closes the screen and drop the item stack
   */
  void closeScreenAndDropStack();

  /**
   * Whether the player can swim.
   *
   * @return {@code true} if the player can swim, otherwise {@code false}
   */
  boolean canSwim();

  /**
   * Sends the horse inventory to the server.
   */
  void sendHorseInventory();

  /**
   * Retrieves the horse jump power.
   *
   * @return The horse jump power.
   */
  float getHorseJumpPower();

  /**
   * Sets the experience stats of this player.
   *
   * @param currentExperience The current experience of this player.
   * @param maxExperience     The max experience of this  player.
   * @param level             The level of this player.
   */
  void setExperienceStats(int currentExperience, int maxExperience, int level);

  /**
   * Whether the player is holding the sneak key.
   *
   * @return {@code true} if the player is holding the sneak key, otherwise {@code false}
   */
  boolean isHoldingSneakKey();

  /**
   * Retrieves the statistics manager of this player.
   *
   * @return The statistics manager of this player.
   */
  // TODO: 06.09.2020 Replaces the Object to StatisticsManager when the Statistics API is ready.
  Object getStatistics();

  /**
   * Retrieves the recipe book of this player.
   *
   * @return The recipe book of this player.
   */
  // TODO: 06.09.2020 Replaces the Object to ClientRecipeBook/RecipeBook when the Item API is ready.
  Object getRecipeBook();

  /**
   * Removes the highlight a recipe.
   *
   * @param recipe The recipe to removing the highlighting.
   */
  // TODO: 06.09.2020 Replaces the Object to Recipe when the Item API is ready.
  void removeRecipeHighlight(Object recipe);

  /**
   * Updates the health of this player.
   * <br><br>
   * <b>Note:</b> This is only on the client side.
   *
   * @param health The new health of this player.
   */
  void updatePlayerHealth(float health);

  /**
   * Updates the entity action state of this player.
   */
  void updateEntityActionState();

  /**
   * Sends a plugin message to the server.
   *
   * @param channel The name of this channel
   * @param data    The data to be written into the channel
   */
  void sendPluginMessage(String channel, byte[] data);

  /**
   * Retrieves the network communicator of this player.<br>
   * The network communicator allows this player to send packets to the server.
   *
   * @return The network communicator of this player.
   */
  Object getConnection();

  /**
   * Retrieves the current biome name of this player.
   *
   * @return The current biome name or {@code null}
   */
  String getBiome();

  /**
   * Whether the selected item can be dropped.
   *
   * @param dropEntireStack Whether the entire stack can be dropped.
   * @return {@code true} if the selected item can be dropped, otherwise {@code false}
   */
  boolean drop(boolean dropEntireStack);

  /**
   * Retrieves the dropped item as an entity.
   *
   * @param droppedItem The dropped item
   * @param traceItem   Whether the item can be traced.
   * @return The dropped item as an entity, or {@code null}
   */
  // TODO: 05.09.2020 (Method Type) Replaces the Object to ItemEntity when the (Entity API?) is ready
  Object dropItem(ItemStack droppedItem, boolean traceItem);

  /**
   * Retrieves the dropped item as an entity.
   *
   * @param droppedItem The dropped item
   * @param dropAround  If {@code true}, the item will be thrown in a random direction
   *                    from the entity regardless of which direction the entity is facing
   * @param traceItem   Whether the item can be traced.
   * @return The dropped item as an entity, or {@code null}
   */
  // TODO: 05.09.2020 (Method Type) Replaces the Object to ItemEntity when the (Entity API?) is ready
  Object dropItem(ItemStack droppedItem, boolean dropAround, boolean traceItem);

  /**
   * Finds shootable items in the inventory of this player.
   *
   * @param shootable The item to be fired.
   * @return an item to be fired or an empty item.
   */
  ItemStack findAmmo(ItemStack shootable);

  /**
   * Whether the player can pick up the item.
   *
   * @param itemStack The item to be pick up
   * @return {@code true} if the player can pick up the item, otherwise {@code false}
   */
  boolean canPickUpItem(ItemStack itemStack);


  /**
   * Opens a sign editor.
   *
   * @param signTileEntity The sign to be edited.
   */
  // TODO: 06.09.2020 Replaces the Object to SignTileEntity when the Entity API is ready
  void openSignEditor(Object signTileEntity);

  /**
   * Opens a minecart command block.
   *
   * @param commandBlock The minecart command block to be opened.
   */
  // TODO: 06.09.2020 Replaces the Object to CommandBlockLogic when the Entity API / Command API is ready
  void openMinecartCommandBlock(Object commandBlock);

  /**
   * Opens a command block.
   *
   * @param commandBlock The command block to be opened.
   */
  // TODO: 06.09.2020 Replaces the Object to CommandBlockTileEntity when the Entity API / Command API is ready
  void openCommandBlock(Object commandBlock);

  /**
   * Opens a structure block.
   *
   * @param structureBlock The structure block to be opened.
   */
  // TODO: 06.09.2020 Replaces the Object to StructureBlockTileEntity when the Entity API is ready
  void openStructureBlock(Object structureBlock);

  /**
   * Opens a jigsaw.
   *
   * @param jigsaw The jigsaw to be opened.
   */
  // TODO: 06.09.2020 Replaces the Object to JigsawTileEntity when the Entity API is ready
  void openJigsaw(Object jigsaw);

  /**
   * Opens a horse inventory
   *
   * @param horse     The horse that has an inventory
   * @param inventory Inventory of the horse
   */
  // TODO: 06.09.2020 (Parameter 1) Replaces the Object to AbstractHorseEntity when the Entity API is ready
  // TODO: 06.09.2020 (Parameter 2) Replaces the Object to Inventory when the Inventory API is ready
  void openHorseInventory(Object horse, Object inventory);

  /**
   * Opens a merchant inventory.
   *
   * @param merchantOffers  The offers of the merchant
   * @param container       The container identifier for this merchant
   * @param levelProgress   The level progress of this merchant.<br>
   *                        <b>Note:</b><br>
   *                        1 = Novice<br>
   *                        2 = Apprentice<br>
   *                        3 = Journeyman<br>
   *                        4 = Expert<br>
   *                        5 = Master
   * @param experience      The total experience for this villager (Always 0 for the wandering trader)
   * @param regularVillager {@code True} if this is a regular villager,
   *                        otherwise {@code false} for the wandering trader. When {@code false},
   *                        hides the villager level  and some other GUI elements
   * @param refreshable     {@code True} for regular villagers and {@code false} for the wandering trader.
   *                        If {@code true}, the "Villagers restock up to two times per day".
   */
  // TODO: 06.09.2020  (Parameter 1) Replaces the Object to MerchantOffers when the (Item API / Entity API)? is ready
  void openMerchantInventory(
          Object merchantOffers,
          int container,
          int levelProgress,
          int experience,
          boolean regularVillager,
          boolean refreshable
  );

  /**
   * Opens a book.
   *
   * @param itemStack The item stack which should be a book.
   * @param hand      The hand of this player.
   */
  void openBook(ItemStack itemStack, Hand hand);

  /**
   * Retrieves the opened container of this player.
   *
   * @return The opened container of this player.
   */
  // TODO: 09.09.2020 Replaces the Object to Container when the Inventory/Item API is ready
  Object getOpenedContainer();

  /**
   * Retrieves the container of this player.
   *
   * @return The container of this player.
   */
  // TODO: 09.09.2020 Replaces the Object to Container when the Inventory/Item API is ready
  Object getPlayerContainer();

  /**
   * Retrieves the previous camera yaw of this player.
   *
   * @return The previous camera yaw of this player.
   */
  float getPrevCameraYaw();

  /**
   * Retrieves the camera yaw of this player.
   *
   * @return The camera yaw of this player.
   */
  float getCameraYaw();

  /**
   * Retrieves the previous chasing position X-axis of this player.
   *
   * @return The previous chasing position X-axis  of this player.
   */
  double getPrevChasingPosX();

  /**
   * Retrieves the previous chasing position Y-axis of this player.
   *
   * @return The previous chasing position Y-axis  of this player.
   */
  double getPrevChasingPosY();

  /**
   * Retrieves the previous chasing position Z-axis of this player.
   *
   * @return The previous chasing position Z-axis  of this player.
   */
  double getPrevChasingPosZ();

  /**
   * Retrieves the chasing position X-axis of this player.
   *
   * @return The chasing position X-axis  of this player.
   */
  double getChasingPosX();

  /**
   * Retrieves the chasing position Y-axis of this player.
   *
   * @return The chasing position Y-axis  of this player.
   */
  double getChasingPosY();

  /**
   * Retrieves the chasing position Z-axis of this player.
   *
   * @return The chasing position Z-axis  of this player.
   */
  double getChasingPosZ();


}
