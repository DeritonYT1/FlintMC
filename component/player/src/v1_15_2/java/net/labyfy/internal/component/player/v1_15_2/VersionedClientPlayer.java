package net.labyfy.internal.component.player.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.netty.buffer.Unpooled;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.inventory.player.PlayerInventory;
import net.labyfy.component.player.ClientPlayer;
import net.labyfy.component.player.PlayerSkinProfile;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.overlay.TabOverlay;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.util.*;
import net.labyfy.component.player.serializer.util.sound.SoundCategorySerializer;
import net.labyfy.component.player.serializer.util.sound.SoundSerializer;
import net.labyfy.component.player.util.EntityPose;
import net.labyfy.component.player.util.GameMode;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.player.util.PlayerClothing;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.player.util.sound.SoundCategory;
import net.labyfy.component.world.ClientWorld;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffers;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CChatMessagePacket;
import net.minecraft.network.play.client.CCustomPayloadPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 1.15.2 implementation of a minecraft player.
 */
@Singleton
@Implement(value = ClientPlayer.class, version = "1.15.2")
public class VersionedClientPlayer implements ClientPlayer {

  private final HandSerializer<net.minecraft.util.Hand> handSerializer;
  private final HandSideSerializer<HandSide> handSideSerializer;
  private final GameModeSerializer<GameType> gameModeSerializer;
  private final GameProfileSerializer<com.mojang.authlib.GameProfile> gameProfileSerializer;
  private final MinecraftComponentMapper minecraftComponentMapper;
  private final NetworkPlayerInfo.Provider networkPlayerInfoProvider;
  private final PlayerClothingSerializer<PlayerModelPart> playerClothingSerializer;
  private final PoseSerializer<Pose> poseSerializer;
  private final SoundCategorySerializer<net.minecraft.util.SoundCategory> soundCategorySerializer;
  private final SoundSerializer<SoundEvent> soundSerializer;
  private final TabOverlay tabOverlay;

  private final ClientWorld clientWorld;
  private final PlayerInventory playerInventory;

  private NetworkPlayerInfo networkPlayerInfo;

  @Inject
  public VersionedClientPlayer(
          HandSerializer handSerializer,
          HandSideSerializer handSideSerializer,
          GameModeSerializer gameModeSerializer,
          GameProfileSerializer gameProfileSerializer,
          MinecraftComponentMapper minecraftComponentMapper,
          NetworkPlayerInfo.Provider networkPlayerInfoProvider,
          PlayerClothingSerializer playerClothingSerializer,
          PoseSerializer poseSerializer,
          SoundCategorySerializer soundCategorySerializer,
          SoundSerializer soundSerializer,
          TabOverlay tabOverlay,
          ClientWorld clientWorld, PlayerInventory playerInventory) {
    this.handSerializer = handSerializer;
    this.handSideSerializer = handSideSerializer;
    this.gameModeSerializer = gameModeSerializer;
    this.gameProfileSerializer = gameProfileSerializer;
    this.minecraftComponentMapper = minecraftComponentMapper;
    this.networkPlayerInfoProvider = networkPlayerInfoProvider;
    this.playerClothingSerializer = playerClothingSerializer;
    this.poseSerializer = poseSerializer;
    this.soundCategorySerializer = soundCategorySerializer;
    this.soundSerializer = soundSerializer;
    this.tabOverlay = tabOverlay;
    this.clientWorld = clientWorld;
    this.playerInventory = playerInventory;
  }

  /**
   * Retrieves the inventory of this player.
   *
   * @return The inventory of this player
   */
  @Override
  public PlayerInventory getPlayerInventory() {
    return this.playerInventory;
  }

  /**
   * Retrieves the armor contents of this player.
   *
   * @return The armor contents of this player.
   */
  @Override
  public List<Object> getArmorContents() {
    return Collections.singletonList(Minecraft.getInstance().player.inventory.armorInventory);
  }

  /**
   * Retrieves the experience level of this player.
   *
   * @return The experience level of this player
   */
  @Override
  public int getExperienceLevel() {
    return Minecraft.getInstance().player.experienceLevel;
  }

  /**
   * Retrieves the experience total of this player.
   *
   * @return The experience total of this player
   */
  @Override
  public int getExperienceTotal() {
    return Minecraft.getInstance().player.experienceTotal;
  }

  /**
   * Retrieves the experience of this player.
   *
   * @return The experience of this player.
   */
  @Override
  public float getExperience() {
    return Minecraft.getInstance().player.experience;
  }

  /**
   * Retrieves the language of this player.
   *
   * @return The language of this player
   */
  @Override
  public String getLocale() {
    return Minecraft.getInstance().getLanguageManager().getCurrentLanguage().getName();
  }

  /**
   * Retrieves the tab overlay of this player.
   *
   * @return The tab overlay of this player.
   */
  @Override
  public TabOverlay getTabOverlay() {
    return this.tabOverlay;
  }

  /**
   * Whether the player has any information
   *
   * @return {@code true} if the player has any information, otherwise {@code false}
   */
  @Override
  public boolean hasPlayerInfo() {
    return this.getNetworkPlayerInfo() != null;
  }

  /**
   * Retrieves the <b>FOV</b> modifier of this player.
   *
   * @return The fov modifier of this player
   */
  @Override
  public float getFovModifier() {
    return Minecraft.getInstance().player.getFovModifier();
  }

  /**
   * Whether auto jump is enabled.
   *
   * @return {@code true} if auto jump is enabled, otherwise {@code false}
   */
  @Override
  public boolean isAutoJumpEnabled() {
    return Minecraft.getInstance().player.isAutoJumpEnabled();
  }

  /**
   * Whether the player rides a horse
   *
   * @return {@code true} if the player rides a horse, otherwise {@code false}
   */
  @Override
  public boolean isRidingHorse() {
    return Minecraft.getInstance().player.isRidingHorse();
  }

  /**
   * Whether the player rows a boat
   *
   * @return {@code true} if the player rows a boat, otherwise {@code false}
   */
  @Override
  public boolean isRowingBoat() {
    return Minecraft.getInstance().player.isRowingBoat();
  }

  /**
   * Whether the player's death screen is shown
   *
   * @return {@code true} if the player's death screen is shown, otherwise {@code false}
   */
  @Override
  public boolean isShowDeathScreen() {
    return Minecraft.getInstance().player.isShowDeathScreen();
  }

  /**
   * Retrieves the server brand of this player.
   *
   * @return The server brand of this player.
   */
  @Override
  public String getServerBrand() {
    return Minecraft.getInstance().player.getServerBrand();
  }

  /**
   * Sets the server brand of this player.
   *
   * @param brand The new server brand.
   */
  @Override
  public void setServerBrand(String brand) {
    Minecraft.getInstance().player.setServerBrand(brand);
  }

  /**
   * Retrieves the water brightness of this player.
   *
   * @return The water brightness of this player.
   */
  @Override
  public float getWaterBrightness() {
    return Minecraft.getInstance().player.getWaterBrightness();
  }

  /**
   * Retrieves the previous time in portal of this player.
   *
   * @return The previous time in portal of this player.
   */
  @Override
  public float getPrevTimeInPortal() {
    return Minecraft.getInstance().player.prevTimeInPortal;
  }

  /**
   * Retrieves the time in portal of this player.
   *
   * @return The time in portal of this player.
   */
  @Override
  public float getTimeInPortal() {
    return Minecraft.getInstance().player.timeInPortal;
  }

  /**
   * Sends a message to the chat
   *
   * @param content The message content
   */
  @Override
  public void sendMessage(String content) {
    Minecraft.getInstance().player.sendChatMessage(content);
  }

  /**
   * Performs a command
   *
   * @param command The command to perform
   * @return {@code true} if the command was performed, otherwise {@code false}
   */
  @Override
  public boolean performCommand(String command) {
    if (!command.startsWith("/")) return false;

    Minecraft.getInstance().getConnection().sendPacket(new CChatMessagePacket(command));

    return true;
  }

  /**
   * Closes the screen and drop the item stack
   */
  @Override
  public void closeScreenAndDropStack() {
    Minecraft.getInstance().player.closeScreenAndDropStack();
  }

  /**
   * Whether the player can swim.
   *
   * @return {@code true} if the player can swim, otherwise {@code false}
   */
  @Override
  public boolean canSwim() {
    return Minecraft.getInstance().player.canSwim();
  }

  /**
   * Sends the horse inventory to the server.
   */
  @Override
  public void sendHorseInventory() {
    Minecraft.getInstance().player.sendHorseInventory();
  }

  /**
   * Retrieves the horse jump power.
   *
   * @return The horse jump power.
   */
  @Override
  public float getHorseJumpPower() {
    return Minecraft.getInstance().player.getHorseJumpPower();
  }

  /**
   * Sets the experience stats of this player.
   *
   * @param currentExperience The current experience of this player.
   * @param maxExperience     The max experience of this  player.
   * @param level             The level of this player.
   */
  @Override
  public void setExperienceStats(int currentExperience, int maxExperience, int level) {
    Minecraft.getInstance().player.setXPStats(currentExperience, maxExperience, level);
  }

  /**
   * Whether the player is holding the sneak key.
   *
   * @return {@code true} if the player is holding the sneak key, otherwise {@code false}
   */
  @Override
  public boolean isHoldingSneakKey() {
    return Minecraft.getInstance().player.func_228354_I_();
  }

  /**
   * Retrieves the statistics manager of this player.
   *
   * @return The statistics manager of this player.
   */
  @Override
  public Object getStatistics() {
    return Minecraft.getInstance().player.getStats();
  }

  /**
   * Retrieves the recipe book of this player.
   *
   * @return The recipe book of this player.
   */
  @Override
  public Object getRecipeBook() {
    return Minecraft.getInstance().player.getRecipeBook();
  }

  /**
   * Removes the highlight a recipe.
   *
   * @param recipe The recipe to removing the highlighting.
   */
  @Override
  public void removeRecipeHighlight(Object recipe) {
    Minecraft.getInstance().player.removeRecipeHighlight((IRecipe<?>) recipe);
  }

  /**
   * Updates the health of this player.
   * <br><br>
   * <b>Note:</b> This is only on the client side.
   *
   * @param health The new health of this player.
   */
  @Override
  public void updatePlayerHealth(float health) {
    Minecraft.getInstance().player.setPlayerSPHealth(health);
  }

  /**
   * Sends the abilities of this player to the server.
   */
  @Override
  public void sendPlayerAbilities() {
    Minecraft.getInstance().player.sendPlayerAbilities();
  }

  /**
   * Updates the entity action state of this player.
   */
  @Override
  public void updateEntityActionState() {
    Minecraft.getInstance().player.updateEntityActionState();
  }

  /**
   * Sends a plugin message to the server.
   *
   * @param channel The name of this channel
   * @param data    The data to be written into the channel
   */
  @Override
  public void sendPluginMessage(String channel, byte[] data) {
    Minecraft.getInstance().player.connection.sendPacket(
            new CCustomPayloadPacket(
                    new ResourceLocation(channel),
                    new PacketBuffer(Unpooled.wrappedBuffer(data))
            )
    );
  }

  /**
   * Retrieves the network communicator of this player.<br>
   * The network communicator allows this player to send packets to the server.
   *
   * @return The network communicator of this player.
   */
  @Override
  public Object getConnection() {
    return Minecraft.getInstance().player.connection;
  }

  /**
   * Sends a status message to this player.
   *
   * @param component The message for this status.
   * @param actionBar Whether to send to the action bar.
   */
  @Override
  public void sendStatusMessage(ChatComponent component, boolean actionBar) {
    Minecraft.getInstance().player.sendStatusMessage(
            (ITextComponent) this.minecraftComponentMapper.toMinecraft(component),
            actionBar
    );
  }

  /**
   * Opens a sign editor.
   *
   * @param signTileEntity The sign to be edited.
   */
  @Override
  public void openSignEditor(Object signTileEntity) {
    Minecraft.getInstance().player.openSignEditor((SignTileEntity) signTileEntity);
  }

  /**
   * Opens a minecart command block.
   *
   * @param commandBlock The minecart command block to be opened.
   */
  @Override
  public void openMinecartCommandBlock(Object commandBlock) {
    Minecraft.getInstance().player.openMinecartCommandBlock((CommandBlockLogic) commandBlock);
  }

  /**
   * Opens a command block.
   *
   * @param commandBlock The command block to be opened.
   */
  @Override
  public void openCommandBlock(Object commandBlock) {
    Minecraft.getInstance().player.openCommandBlock((CommandBlockTileEntity) commandBlock);
  }

  /**
   * Opens a structure block.
   *
   * @param structureBlock The structure block to be opened.
   */
  @Override
  public void openStructureBlock(Object structureBlock) {
    Minecraft.getInstance().player.openStructureBlock((StructureBlockTileEntity) structureBlock);
  }

  /**
   * Opens a jigsaw.
   *
   * @param jigsaw The jigsaw to be opened.
   */
  @Override
  public void openJigsaw(Object jigsaw) {
    Minecraft.getInstance().player.openJigsaw((JigsawTileEntity) jigsaw);
  }

  /**
   * Opens a book.
   *
   * @param itemStack The item stack which should be a book.
   * @param hand      The hand of this player.
   */
  @Override
  public void openBook(Object itemStack, Hand hand) {
    Minecraft.getInstance().player.openBook((ItemStack) itemStack, this.handSerializer.serialize(hand));
  }

  /**
   * Opens a horse inventory
   *
   * @param horse     The horse that has an inventory
   * @param inventory Inventory of the horse
   */
  @Override
  public void openHorseInventory(Object horse, Object inventory) {
    Minecraft.getInstance().player.openHorseInventory(
            (AbstractHorseEntity) horse,
            (IInventory) inventory
    );
  }

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
  @Override
  public void openMerchantInventory(
          Object merchantOffers,
          int container,
          int levelProgress,
          int experience,
          boolean regularVillager,
          boolean refreshable
  ) {
    Minecraft.getInstance().player.openMerchantContainer(
            container,
            (MerchantOffers) merchantOffers,
            levelProgress,
            experience,
            regularVillager,
            refreshable
    );
  }

  /**
   * Retrieves the current biome name of this player.
   *
   * @return The current biome name or {@code null}
   */
  @Override
  public String getBiome() {
    World world = Minecraft.getInstance().world;
    Entity renderViewEntity = Minecraft.getInstance().getRenderViewEntity();
    if (renderViewEntity == null) return null;
    BlockPos blockPos = new BlockPos(renderViewEntity);

    if (world != null) {
      String biomePath = Registry.BIOME.getKey(world.getBiome(blockPos)).getPath();
      String[] split = biomePath.split("_");
      StringBuilder builder = new StringBuilder();

      for (int i = 0; i < split.length; i++) {
        String biomeName = split[i];
        biomeName = biomeName.substring(0, 1).toUpperCase() + biomeName.substring(1).toLowerCase();
        if (i == split.length - 1) {
          builder.append(biomeName);
          break;
        }
        builder.append(biomeName).append(" ");
      }

      return builder.toString();
    }

    return null;
  }

  /**
   * Retrieves the world of this player.
   *
   * @return The world of this player.
   */
  @Override
  public ClientWorld getWorld() {
    return this.clientWorld;
  }

  /**
   * Retrieves the game profile of this player.
   *
   * @return The game profile of this player.
   */
  @Override
  public GameProfile getGameProfile() {
    return this.gameProfileSerializer.deserialize(Minecraft.getInstance().player.getGameProfile());
  }

  /**
   * Retrieves the name of this player.
   *
   * @return The name of this player
   */
  @Override
  public ChatComponent getName() {
    return this.minecraftComponentMapper.fromMinecraft(Minecraft.getInstance().player.getName());
  }

  /**
   * Retrieves the display name of this player
   *
   * @return The display name of this player
   */
  @Override
  public ChatComponent getDisplayName() {
    return this.minecraftComponentMapper.fromMinecraft(Minecraft.getInstance().player.getDisplayName());
  }

  /**
   * Retrieves the display name and the unique identifier of this player.
   *
   * @return The display name and the unique identiifer of this player.
   */
  @Override
  public ChatComponent getDisplayNameAndUniqueId() {
    return this.minecraftComponentMapper.fromMinecraft(Minecraft.getInstance().player.getDisplayNameAndUUID());
  }

  /**
   * Retrieves the unique identifier of this entity.
   *
   * @return The unique identifier of this entity.
   */
  @Override
  public UUID getUniqueId() {
    return Minecraft.getInstance().player.getUniqueID();
  }

  @Override
  public UUID getPlayerUniqueId() {
    return this.getGameProfile().getUniqueId();
  }

  /**
   * Retrieves the health of this player.
   *
   * @return The health of this player
   */
  @Override
  public float getHealth() {
    return Minecraft.getInstance().player.getHealth();
  }

  /**
   * Retrieves the list name of this player.
   *
   * @return The list name of this player.
   */
  @Override
  public String getPlayerListName() {
    return Minecraft.getInstance().player.getScoreboardName();
  }

  /**
   * Retrieves the world time.
   *
   * @return The world time.
   */
  @Override
  public long getPlayerTime() {
    return this.clientWorld.getDayTime();
  }

  /**
   * Retrieves the x position of this player.
   *
   * @return The x position of this player
   */
  @Override
  public double getX() {
    return Minecraft.getInstance().player.getPosX();
  }

  /**
   * Retrieves the y position of this player.
   *
   * @return The y position of this player
   */
  @Override
  public double getY() {
    return Minecraft.getInstance().player.getPosY();
  }

  /**
   * Retrieves the z position of this player.
   *
   * @return The z position of this playe
   */
  @Override
  public double getZ() {
    return Minecraft.getInstance().player.getPosZ();
  }

  /**
   * Retrieves the pitch of this player.
   *
   * @return The pitch of this player.
   */
  @Override
  public float getPitch() {
    return Minecraft.getInstance().player.getPitchYaw().x;
  }

  /**
   * Retrieves the pitch of this player.
   *
   * @param partialTicks The period of time, in fractions of a tick,
   *                     that has passed since the last full tick
   * @return The pitch of this player.
   */
  @Override
  public float getPitch(float partialTicks) {
    return Minecraft.getInstance().player.getPitch(partialTicks);
  }

  /**
   * Retrieves the yaw of this player.
   *
   * @return The yaw of this player.
   */
  @Override
  public float getYaw() {
    return Minecraft.getInstance().player.getPitchYaw().y;
  }

  /**
   * Retrieves the yaw of this player.
   *
   * @param partialTicks The period of time, in fractions of a tick,
   *                     that has passed since the last full tick
   * @return The yaw of this player.
   */
  @Override
  public float getYaw(float partialTicks) {
    return Minecraft.getInstance().player.getYaw(partialTicks);
  }

  /**
   * Retrieves the rotation yaw head of this player.
   *
   * @return The rotation yaw head of this player.
   */
  @Override
  public float getRotationYawHead() {
    return Minecraft.getInstance().player.getRotationYawHead();
  }

  /**
   * Retrieves the eye height of this player.
   *
   * @return The eye height of this player.
   */
  @Override
  public float getEyeHeight() {
    return Minecraft.getInstance().player.getEyeHeight();
  }

  /**
   * Retrieves the eye height of this player.
   *
   * @param entityPose The current pose for the eye height
   * @return The eye height of this player
   */
  @Override
  public float getEyeHeight(EntityPose entityPose) {
    return Minecraft.getInstance().player.getEyeHeight(this.poseSerializer.serialize(entityPose));
  }

  /**
   * Retrieves the y position of the eyes of this player.
   *
   * @return The y position of the eyes of this player
   */
  @Override
  public double getPosYEye() {
    return Minecraft.getInstance().player.getPosYEye();
  }

  /**
   * Retrieves the pose of this player.
   *
   * @return The pose of this player.
   */
  @Override
  public EntityPose getPose() {
    return this.poseSerializer.deserialize(Minecraft.getInstance().player.getPose());
  }

  /**
   * Whether the player is in spectator mode.
   *
   * @return {@code true} if the player is in the spectator mode, otherwise {@code false}
   */
  @Override
  public boolean isSpectator() {
    return Minecraft.getInstance().player.isSpectator();
  }

  /**
   * Whether the player is in creative mode.
   *
   * @return {@code true} if the player is in the creative mode, otherwise {@code false}
   */
  @Override
  public boolean isCreative() {
    return Minecraft.getInstance().player.isCreative();
  }

  /**
   * Retrieves the active hand of this player.
   *
   * @return The active hand of this player
   */
  @Override
  public Hand getActiveHand() {
    return this.handSerializer.deserialize(Minecraft.getInstance().player.getActiveHand());
  }

  /**
   * Whether a hand is active.
   *
   * @return {@code true} if a hand is active, otherwise {@code false}
   */
  @Override
  public boolean isHandActive() {
    return Minecraft.getInstance().player.isHandActive();
  }

  /**
   * Retrieves the active item stack of this player.
   *
   * @return The active item stack of this player
   */
  // TODO: 04.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready
  @Override
  public Object getActiveItemStack() {
    return Minecraft.getInstance().player.getActiveItemStack();
  }

  /**
   * Retrieves use count of the item.
   *
   * @return The use count of the item
   */
  @Override
  public int getItemInUseCount() {
    return Minecraft.getInstance().player.getItemInUseCount();
  }

  /**
   * Retrieves the maximal use count of the item.
   *
   * @return The maximal use count of this item.
   */
  @Override
  public int getItemInUseMaxCount() {
    return Minecraft.getInstance().player.getItemInUseMaxCount();
  }

  /**
   * Retrieves the food level of this player.
   *
   * @return The food level of this player.
   */
  @Override
  public int getFoodLevel() {
    return Minecraft.getInstance().player.getFoodStats().getFoodLevel();
  }

  /**
   * Retrieves the saturation of this player.
   *
   * @return The saturation of this player.
   */
  @Override
  public float getSaturationLevel() {
    return Minecraft.getInstance().player.getFoodStats().getSaturationLevel();
  }

  /**
   * Whether the player needs food
   *
   * @return {@code true} if the player needs food, otherwise {@code false}
   */
  @Override
  public boolean needFood() {
    return Minecraft.getInstance().player.getFoodStats().needFood();
  }

  /**
   * Whether the player is in a three dimensional room to render
   *
   * @param x The x position
   * @param y The y position
   * @param z The z position
   * @return {@code true} if the player is in the three dimensional room, otherwise {@code false}
   */
  @Override
  public boolean isRangeToRender3d(double x, double y, double z) {
    return Minecraft.getInstance().player.isInRangeToRender3d(x, y, z);
  }

  /**
   * Whether the player is in the range to render.
   *
   * @param distance The distance
   * @return {@code true} if the player is in range, otherwise {@code false}
   */
  @Override
  public boolean isInRangeToRender(double distance) {
    return Minecraft.getInstance().player.isInRangeToRenderDist(distance);
  }

  /**
   * Whether the player is alive
   *
   * @return {@code true} if the player is alive, otherwise {@code false}
   */
  @Override
  public boolean isAlive() {
    return Minecraft.getInstance().player.isAlive();
  }

  /**
   * Whether the player is sprinting
   *
   * @return {@code true} if the player is sprinting, otherwise {@code false}
   */
  @Override
  public boolean isSprinting() {
    return Minecraft.getInstance().player.isSprinting();
  }

  /**
   * Whether the player is on a ladder
   *
   * @return {@code true} if the player is on ladder, otherwise {@code false}
   */
  @Override
  public boolean isOnLadder() {
    return Minecraft.getInstance().player.isOnLadder();
  }

  /**
   * Whether the player is sleeping
   *
   * @return {@code true} if the player is sleeping, otherwise {@code false}
   */
  @Override
  public boolean isSleeping() {
    return Minecraft.getInstance().player.isSleeping();
  }

  /**
   * Whether the player is swimming
   *
   * @return {@code true} if the player is swimming, otherwise {@code false}
   */
  @Override
  public boolean isSwimming() {
    return Minecraft.getInstance().player.isSwimming();
  }

  /**
   * Whether the player is wet
   *
   * @return {@code true} if the player is wet, otherwise {@code false}
   */
  @Override
  public boolean isWet() {
    return Minecraft.getInstance().player.isWet();
  }

  /**
   * Whether the player is crouched
   *
   * @return {@code true} if the player is crouched, otherwise {@code false}
   */
  @Override
  public boolean isCrouching() {
    return Minecraft.getInstance().player.isCrouching();
  }

  /**
   * Whether the player glows
   *
   * @return {@code true} if the player glows, otherwise {@code false}
   */
  @Override
  public boolean isGlowing() {
    return Minecraft.getInstance().player.isGlowing();
  }

  /**
   * Whether the player is sneaked
   *
   * @return {@code true} if the player is sneaked, otherwise {@code false}
   */
  @Override
  public boolean isSneaking() {
    return Minecraft.getInstance().player.isSneaking();
  }

  /**
   * Whether the player is in water
   *
   * @return {@code true} if the player is in water, otherwise {@code false}
   */
  @Override
  public boolean isInWater() {
    return Minecraft.getInstance().player.isInWater();
  }

  /**
   * Whether the player is in lava
   *
   * @return {@code true} if the player is in lava, otherwise {@code false}
   */
  @Override
  public boolean isInLava() {
    return Minecraft.getInstance().player.isInLava();
  }

  /**
   * Whether the player is wearing the given clothing.
   *
   * @param playerClothing The clothing that should be worn.
   * @return {@code true} if the player is wearing the clothing, otherwise {@code false}
   */
  @Override
  public boolean isWearing(PlayerClothing playerClothing) {
    return Minecraft.getInstance().player.isWearing(this.playerClothingSerializer.serialize(playerClothing));
  }

  /**
   * Whether the player is invisible
   *
   * @return {@code true} if the player is invisible, otherwise {@code false}
   */
  @Override
  public boolean isInvisible() {
    return Minecraft.getInstance().player.isInvisible();
  }

  /**
   * Sets the player invisible
   *
   * @param invisible The new state for invisible
   */
  @Override
  public void setInvisible(boolean invisible) {
    Minecraft.getInstance().player.setInvisible(invisible);
  }

  /**
   * Whether the player is burning
   *
   * @return {@code true} if the player is burning, otherwise {@code false}
   */
  @Override
  public boolean isBurning() {
    return Minecraft.getInstance().player.isBurning();
  }

  /**
   * Whether the player is flying with an elytra.
   *
   * @return {@code true} if the player is flying with an elytra, otherwise {@code false}
   */
  @Override
  public boolean isElytraFlying() {
    return Minecraft.getInstance().player.isElytraFlying();
  }

  /**
   * Sets the absorption amount of this player.
   *
   * @param amount The new absorption amount.
   */
  @Override
  public void setAbsorptionAmount(float amount) {
    Minecraft.getInstance().player.setAbsorptionAmount(amount);
  }

  /**
   * Retrieves the absorption amount of this player.
   *
   * @return Theabsorption amount of this player.
   */
  @Override
  public float getAbsorptionAmount() {
    return Minecraft.getInstance().player.getAbsorptionAmount();
  }

  /**
   * Retrieves the total armor value of this player.
   *
   * @return The total armor value of this player.
   */
  @Override
  public int getTotalArmorValue() {
    return Minecraft.getInstance().player.getTotalArmorValue();
  }

  /**
   * Prints a message in this player chat
   *
   * @param component The message to print
   */
  @Override
  public void sendMessage(ChatComponent component) {
    Minecraft.getInstance().player.sendMessage((ITextComponent) this.minecraftComponentMapper.toMinecraft(component));
  }

  /**
   * Retrieves the network player info of this player.
   *
   * @return The network player info of this  player
   */
  @Override
  public NetworkPlayerInfo getNetworkPlayerInfo() {
    if (this.networkPlayerInfo == null) {
      this.networkPlayerInfo = this.networkPlayerInfoProvider.get(this);
    }

    return this.networkPlayerInfo;
  }

  /**
   * Plays a sound to the player.
   *
   * @param sound  The sound to play.
   * @param volume The volume of this sound.
   * @param pitch  The pitch of this sound.
   */
  @Override
  public void playSound(Sound sound, float volume, float pitch) {
    this.playSound(sound, this.getSoundCategory(), volume, pitch);
  }

  /**
   * Plays a sound to the player.
   *
   * @param sound    The sound to play.
   * @param category The category for this sound.
   * @param volume   The volume of this sound.
   * @param pitch    The pitch of this sound.
   */
  @Override
  public void playSound(Sound sound, SoundCategory category, float volume, float pitch) {
    Minecraft.getInstance().player.playSound(
            this.soundSerializer.serialize(sound),
            this.soundCategorySerializer.serialize(category),
            volume,
            pitch
    );
  }

  /**
   * Retrieves the fall distance of this player.
   *
   * @return The fall distance of this player.
   */
  @Override
  public float getFallDistance() {
    return Minecraft.getInstance().player.fallDistance;
  }

  /**
   * Retrieves the maximal fall distance of this player.
   *
   * @return The maximal fall distance of this player.
   */
  @Override
  public int getMaxFallDistance() {
    return Minecraft.getInstance().player.getMaxFallHeight();
  }

  /**
   * Swings the arm through the given hand.
   *
   * @param hand The hand to swing.
   */
  @Override
  public void swingArm(Hand hand) {
    Minecraft.getInstance().player.swingArm(this.handSerializer.serialize(hand));
  }

  /**
   * Retrieves the maximal in portal time of this player.
   *
   * @return The maximal in portal time of this player.
   */
  @Override
  public int getMaxInPortalTime() {
    return Minecraft.getInstance().player.getMaxInPortalTime();
  }

  /**
   * Retrieves the fly speed of this player.
   *
   * @return The fly speed of this player.
   */
  @Override
  public float getFlySpeed() {
    return Minecraft.getInstance().player.abilities.getFlySpeed();
  }

  /**
   * Sets the fly speed of this player.
   *
   * @param speed The new fly speed.
   */
  @Override
  public void setFlySpeed(float speed) {
    Minecraft.getInstance().player.abilities.setFlySpeed(speed);
  }

  /**
   * Retrieves the walk speed of this player.
   *
   * @return The walk speed of this player.
   */
  @Override
  public float getWalkSpeed() {
    return Minecraft.getInstance().player.abilities.getWalkSpeed();
  }

  /**
   * Sets the walk speed of this player.
   *
   * @param speed The new walk speed.
   */
  @Override
  public void setWalkSpeed(float speed) {
    Minecraft.getInstance().player.abilities.setWalkSpeed(speed);
  }

  /**
   * Whether the player is on the ground.
   *
   * @return {@code true} if the player is on the ground, otherwise {@code false}
   */
  @Override
  public boolean isOnGround() {
    return Minecraft.getInstance().player.onGround;
  }

  /**
   * Retrieves the luck of this player.
   *
   * @return The luck of this player.
   */
  @Override
  public float getLuck() {
    return Minecraft.getInstance().player.getLuck();
  }

  /**
   * Retrieves the primary hand this player.
   *
   * @return The primary hand this player.
   */
  @Override
  public Hand.Side getPrimaryHand() {
    return this.handSideSerializer.deserialize(Minecraft.getInstance().player.getPrimaryHand());
  }

  /**
   * Sets the primary hand of this player.
   *
   * @param side The new primary hand of this player.
   */
  @Override
  public void setPrimaryHand(Hand.Side side) {
    Minecraft.getInstance().player.setPrimaryHand(this.handSideSerializer.serialize(side));
  }

  /**
   * Whether the player can use command block.
   *
   * @return {@code true} if the player can use command blocks, otherwise {@code false}
   */
  @Override
  public boolean canUseCommandBlock() {
    return Minecraft.getInstance().player.canUseCommandBlock();
  }

  /**
   * Retrieves the cooldown period of this player.
   *
   * @return The cooldown period of this player.
   */
  @Override
  public float getCooldownPeriod() {
    return Minecraft.getInstance().player.getCooldownPeriod();
  }

  /**
   * Retrieves the cooled attack strength of this player.
   *
   * @param adjustTicks The ticks to adjust the cooled strength of the attack.
   * @return The cooled attack strength of this player.
   */
  @Override
  public float getCooledAttackStrength(float adjustTicks) {
    return Minecraft.getInstance().player.getCooledAttackStrength(adjustTicks);
  }

  /**
   * Resets the cooldown of this player.
   */
  @Override
  public void resetCooldown() {
    Minecraft.getInstance().player.resetCooldown();
  }

  /**
   * Whether the player has reduced debug.
   *
   * @return {@code true} if the player has reduced debug, otherwise {@code false}
   */
  @Override
  public boolean hasReducedDebug() {
    return Minecraft.getInstance().player.hasReducedDebug();
  }

  /**
   * Sets the reduced debug for this player.
   *
   * @param reducedDebug The new reduced debug.
   */
  @Override
  public void setReducedDebug(boolean reducedDebug) {
    Minecraft.getInstance().player.setReducedDebug(reducedDebug);
  }

  /**
   * Whether the item stack at the slot can be replaced.
   *
   * @param slot      The slot that should be replaced.
   * @param itemStack The item stack to be replaced.
   * @return {@code true} if the item stack can be replaced, otherwise {@code false}
   */
  @Override
  public boolean replaceItemInInventory(int slot, Object itemStack) {
    return Minecraft.getInstance().player.replaceItemInInventory(slot, (ItemStack) itemStack);
  }

  /**
   * Whether the player is pushed by water.
   *
   * @return {@code true} if the player pushed by water, otherwise {@code false}
   */
  @Override
  public boolean isPushedByWater() {
    return Minecraft.getInstance().player.isPushedByWater();
  }

  /**
   * Retrieves an iterable collection of the equipment held by that player.
   *
   * @return An iterable collection of the equipment held by that player.
   */
  @Override
  public Iterable<Object> getHeldEquipment() {
    /*return Minecraft.getInstance().player.getHeldEquipment();*/
    return null;
  }

  /**
   * Retrieves an iterable inventory of that player's armor.
   *
   * @return An iterable inventory of that player's armor.
   */
  @Override
  public Iterable<Object> getArmorInventoryList() {
    /*return Minecraft.getInstance().player.getArmorInventoryList();*/
    return null;
  }

  /**
   * Whether the item stack was added to this main inventory.
   *
   * @param itemStack The item stack to be added
   * @return {@code true} if was the item stack added, otherwise {@code false}
   */
  @Override
  public boolean addItemStackToInventory(Object itemStack) {
    return Minecraft.getInstance().player.addItemStackToInventory((ItemStack) itemStack);
  }

  /**
   * Whether the player is allowed to edit.
   *
   * @return {@code true} if the player is allowed to edit, otherwise {@code false}
   */
  @Override
  public boolean isAllowEdit() {
    return Minecraft.getInstance().player.isAllowEdit();
  }

  /**
   * Whether the player should heal.
   *
   * @return {@code true} if the player should heal, otherwise {@code false}
   */
  @Override
  public boolean shouldHeal() {
    return Minecraft.getInstance().player.shouldHeal();
  }

  /**
   * Whether the player can eat.
   *
   * @param ignoreHunger Whether hunger should be ignored.
   * @return {@code true} if the player can eat, otherwise {@code false}
   */
  @Override
  public boolean canEat(boolean ignoreHunger) {
    return Minecraft.getInstance().player.canEat(ignoreHunger);
  }

  /**
   * Adds the exhaustion of this player.
   *
   * @param exhaustion The exhaustion to be added.
   */
  @Override
  public void addExhaustion(float exhaustion) {
    Minecraft.getInstance().player.addExhaustion(exhaustion);
  }

  /**
   * Adds the experience level to this player.
   *
   * @param levels The levels to be added.
   */
  @Override
  public void addExperienceLevel(int levels) {
    Minecraft.getInstance().player.addExperienceLevel(levels);
  }

  /**
   * Retrieves the experience bar cap of this player.
   *
   * @return The experience bar cap of this player.
   */
  @Override
  public int experienceBarCap() {
    return Minecraft.getInstance().player.xpBarCap();
  }

  /**
   * Retrieves the experience speed of this player.
   *
   * @return The experience speed of this player.
   */
  @Override
  public int getExperienceSpeed() {
    return Minecraft.getInstance().player.getXPSeed();
  }

  /**
   * Gives this player experience points.
   *
   * @param points The points to be assigned
   */
  @Override
  public void giveExperiencePoints(int points) {
    Minecraft.getInstance().player.giveExperiencePoints(points);
  }

  /**
   * Whether can be tried to start the fall flying of this player.
   *
   * @return {@code true} if can be tried to start the fall flying, otherwise {@code false}
   */
  @Override
  public boolean tryToStartFallFlying() {
    return Minecraft.getInstance().player.tryToStartFallFlying();
  }

  /**
   * Starts the fall flying of this player.
   */
  @Override
  public void startFallFlying() {
    Minecraft.getInstance().player.startFallFlying();
  }

  /**
   * Stops the fall flying of this player.
   */
  @Override
  public void stopFallFlying() {
    Minecraft.getInstance().player.stopFallFlying();
  }

  /**
   * Lets the player jump.
   */
  @Override
  public void jump() {
    Minecraft.getInstance().player.jump();
  }

  /**
   * Updates the swimming of this player.
   */
  @Override
  public void updateSwimming() {
    Minecraft.getInstance().player.updateSwimming();
  }

  /**
   * Retrieves the AI move speed of this player.
   *
   * @return The AI move speed of this player.
   */
  @Override
  public float getAIMoveSpeed() {
    return Minecraft.getInstance().player.getAIMoveSpeed();
  }

  /**
   * Adds movement stats to this player.
   *
   * @param x The x position to be added
   * @param y The y position to be added
   * @param z The z position to be added
   */
  @Override
  public void addMovementStat(double x, double y, double z) {
    Minecraft.getInstance().player.addMovementStat(x, y, z);
  }

  /**
   * Whether the spawn is forced.
   *
   * @return {@code true} if the spawn is forced, otherwise {@code false}
   */
  @Override
  public boolean isSpawnForced() {
    return Minecraft.getInstance().player.isSpawnForced();
  }

  /**
   * Whether the player is fully asleep.
   *
   * @return {@code true} if the player is fully  asleep, otherwise {@code false}
   */
  @Override
  public boolean isPlayerFullyAsleep() {
    return Minecraft.getInstance().player.isPlayerFullyAsleep();
  }

  /**
   * Retrieves the sleep timer of this player.
   *
   * @return The sleep timer of this player.
   */
  @Override
  public int getSleepTimer() {
    return Minecraft.getInstance().player.getSleepTimer();
  }

  /**
   * Wakes up this player.
   */
  @Override
  public void wakeUp() {
    Minecraft.getInstance().player.wakeUp();
  }

  /**
   * Wakes up this player or updates all sleeping players.
   *
   * @param updateTimer           Updates the sleep timer
   * @param updateSleepingPlayers Updates all sleeping players.
   */
  @Override
  public void wakeUp(boolean updateTimer, boolean updateSleepingPlayers) {
    Minecraft.getInstance().player.stopSleepInBed(updateTimer, updateSleepingPlayers);
  }

  /**
   * Disables the shield of this player.
   *
   * @param sprinting Whether the player is sprinting.
   */
  @Override
  public void disableShield(boolean sprinting) {
    Minecraft.getInstance().player.disableShield(sprinting);
  }

  /**
   * Stops the ride of this player.
   */
  @Override
  public void stopRiding() {
    Minecraft.getInstance().player.stopRiding();
  }

  /**
   * Whether the player can attack another player.
   *
   * @param player The player to be attacked
   * @return {@code true} if can the player be attacked, otherwise {@code false}
   */
  @Override
  public boolean canAttackPlayer(Object player) {
    return Minecraft.getInstance().player.canAttackPlayer((PlayerEntity) player);
  }

  /**
   * Whether the item has a cooldown.
   *
   * @param item The item to be checked
   * @return {@code true} if the item has a cooldown, otherwise {@code false}
   */
  @Override
  public boolean hasCooldown(Object item) {
    return Minecraft.getInstance().player.getCooldownTracker().hasCooldown((Item) item);
  }

  /**
   * Retrieves the cooldown of the given item.
   *
   * @param item         The item to get the cooldown
   * @param partialTicks The period of time, in fractions of a tick,
   *                     that has passed since the last full tick.
   * @return The cooldown of this given item.
   */
  @Override
  public float getCooldown(Object item, float partialTicks) {
    return Minecraft.getInstance().player.getCooldownTracker().getCooldown((Item) item, partialTicks);
  }

  /**
   * Sets the for the cooldown tracking.
   *
   * @param item  The item for setting the cooldown.
   * @param ticks The ticks, how long the cooldown lasts.
   */
  @Override
  public void setCooldown(Object item, int ticks) {
    Minecraft.getInstance().player.getCooldownTracker().setCooldown((Item) item, ticks);
  }

  /**
   * Retrieves the score of this player.
   *
   * @return The score of this player.
   */
  @Override
  public int getScore() {
    return Minecraft.getInstance().player.getScore();
  }

  /**
   * Sets the score of this player.
   *
   * @param score The new score
   */
  @Override
  public void setScore(int score) {
    Minecraft.getInstance().player.setScore(score);
  }

  /**
   * Adds the score to this player.
   *
   * @param score The score to be added
   */
  @Override
  public void addScore(int score) {
    Minecraft.getInstance().player.addScore(score);
  }

  /**
   * Whether the selected item can be dropped.
   *
   * @param dropEntireStack Whether the entire stack can be dropped.
   * @return {@code true} if the selected item can be dropped, otherwise {@code false}
   */
  @Override
  public boolean drop(boolean dropEntireStack) {
    return Minecraft.getInstance().player.drop(dropEntireStack);
  }

  /**
   * Retrieves the dropped item as an entity.
   *
   * @param droppedItem The dropped item
   * @param traceItem   Whether the item can be traced.
   * @return The dropped item as an entity, or {@code null}
   */
  @Override
  public Object dropItem(Object droppedItem, boolean traceItem) {
    return Minecraft.getInstance().player.dropItem((ItemStack) droppedItem, traceItem);
  }

  /**
   * Retrieves the dropped item as an entity.
   *
   * @param droppedItem The dropped item
   * @param dropAround  If {@code true}, the item will be thrown in a random direction
   *                    from the entity regardless of which direction the entity is facing
   * @param traceItem   Whether the item can be traced.
   * @return The dropped item as an entity, or {@code null}
   */
  @Override
  public Object dropItem(Object droppedItem, boolean dropAround, boolean traceItem) {
    return Minecraft.getInstance().player.dropItem((ItemStack) droppedItem, dropAround, traceItem);
  }

  /**
   * Retrieves the digging speed of the given block state for this player.
   *
   * @param blockState The block state that is to receive the dig speed.
   * @return The digging speed of the block state for this player.
   */
  @Override
  public float getDigSpeed(Object blockState) {
    return Minecraft.getInstance().player.getDigSpeed((BlockState) blockState);
  }

  /**
   * Whether the player can harvest the block.
   *
   * @param blockState The block to be harvested
   * @return {@code true} if the player can harvest the block, otherwise {@code false}.
   */
  @Override
  public boolean canHarvestBlock(Object blockState) {
    return Minecraft.getInstance().player.canHarvestBlock((BlockState) blockState);
  }

  /**
   * Reads the additional of the given compound nbt.
   *
   * @param compoundNBT The compound nbt to be read.
   */
  @Override
  public void readAdditional(Object compoundNBT) {
    Minecraft.getInstance().player.readAdditional((CompoundNBT) compoundNBT);
  }

  /**
   * Writes into the additional of this player.
   *
   * @param compoundNBT The additional to be written.
   */
  @Override
  public void writeAdditional(Object compoundNBT) {
    Minecraft.getInstance().player.writeAdditional((CompoundNBT) compoundNBT);
  }

  /**
   * Finds shootable items in the inventory of this player.
   *
   * @param shootable The item to be fired.
   * @return An item to be fired or an empty item.
   */
  @Override
  public Object findAmmo(Object shootable) {
    return Minecraft.getInstance().player.findAmmo((ItemStack) shootable);
  }

  /**
   * Whether the player can pick up the item.
   *
   * @param itemStack The item to be pick up
   * @return {@code true} if the player can pick up the item, otherwise {@code false}
   */
  @Override
  public boolean canPickUpItem(Object itemStack) {
    return Minecraft.getInstance().player.canPickUpItem((ItemStack) itemStack);
  }

  /**
   * Adds should entity to this player.
   *
   * @param compoundNbt The entity as a compound nbt
   * @return {@code true} if an entity was added to the shoulder, otherwise {@code false}
   */
  @Override
  public boolean addShoulderEntity(Object compoundNbt) {
    return Minecraft.getInstance().player.addShoulderEntity((CompoundNBT) compoundNbt);
  }

  /**
   * Retrieves the entity which is on the left shoulder.
   *
   * @return The entity as a compound nbt.
   */
  @Override
  public Object getLeftShoulderEntity() {
    return Minecraft.getInstance().player.getLeftShoulderEntity();
  }

  /**
   * Retrieves the entity which is on the right shoulder.
   *
   * @return The entity as a compound nbt.
   */
  @Override
  public Object getRightShoulderEntity() {
    return Minecraft.getInstance().player.getRightShoulderEntity();
  }

  /**
   * Retrieves the fire timer of this player.
   *
   * @return The fire timer of this player.
   */
  @Override
  public int getFireTimer() {
    return Minecraft.getInstance().player.getFireTimer();
  }

  /**
   * Retrieves the step height of this player.
   *
   * @return The step height of this player.
   */
  @Override
  public float getStepHeight() {
    return Minecraft.getInstance().player.stepHeight;
  }

  /**
   * Retrieves the x rotate elytra of this player.
   *
   * @return The x rotate elytra of this player.
   */
  @Override
  public float getRotateElytraX() {
    return Minecraft.getInstance().player.rotateElytraX;
  }

  /**
   * Retrieves the y rotate elytra of this player.
   *
   * @return The y rotate elytra of this player.
   */
  @Override
  public float getRotateElytraY() {
    return Minecraft.getInstance().player.rotateElytraY;
  }

  /**
   * Retrieves the z rotate elytra of this player.
   *
   * @return The z rotate elytra of this player.
   */
  @Override
  public float getRotateElytraZ() {
    return Minecraft.getInstance().player.rotateElytraZ;
  }

  /**
   * Whether the player is collided.
   *
   * @return {@code true} if the player is collided, otherwise {@code false}
   */
  @Override
  public boolean isCollided() {
    return Minecraft.getInstance().player.collided;
  }

  /**
   * Whether the player is collided horizontally.
   *
   * @return {@code true} if the player is collided horizontally, otherwise {@code false}
   */
  @Override
  public boolean isCollidedHorizontally() {
    return Minecraft.getInstance().player.collidedHorizontally;
  }

  /**
   * Whether the player is collided vertically.
   *
   * @return {@code true} if the player is collided vertically, otherwise {@code false}
   */
  @Override
  public boolean isCollidedVertically() {
    return Minecraft.getInstance().player.collidedVertically;
  }

  /**
   * Prepare this player for spawning.
   */
  @Override
  public void preparePlayerToSpawn() {
    Minecraft.getInstance().player.preparePlayerToSpawn();
  }

  /**
   * Whether block actions are restricted for this player.
   *
   * @param blockPos    This position of this block
   * @param gameMode    This game mode of this player
   * @return {@code true} if this player has restricted block actions, otherwise {@code false}
   */
  @Override
  public boolean blockActionRestricted(Object blockPos, GameMode gameMode) {
    return Minecraft.getInstance().player.blockActionRestricted(
            Minecraft.getInstance().world,
            (BlockPos) blockPos,
            this.gameModeSerializer.serialize(gameMode)
    );
  }

  /**
   * Spawns sweep particles.
   */
  @Override
  public void spawnSweepParticles() {
    Minecraft.getInstance().player.spawnSweepParticles();
  }

  /**
   * Respawns this player.
   */
  @Override
  public void respawnPlayer() {
    Minecraft.getInstance().player.respawnPlayer();
  }

  /**
   * Updates the game mode of this player.
   * <br>
   * <b>Note:</b> This is only on the server side.
   *
   * @param gameMode The new game of this player
   */
  @Override
  public void updateGameMode(GameMode gameMode) {
    Minecraft.getInstance().player.setGameType(this.gameModeSerializer.serialize(gameMode));
  }

  /**
   * Enchants the given item stack with the cost.
   *
   * @param itemStack The item stack to enchant
   * @param cost      The cost of the enchant
   */
  @Override
  public void enchantItem(Object itemStack, int cost) {
    Minecraft.getInstance().player.onEnchant((ItemStack) itemStack, cost);
  }

  /**
   * Retrieves the opened container of this player.
   *
   * @return The opened container of this player.
   */
  @Override
  public Object getOpenedContainer() {
    return Minecraft.getInstance().player.openContainer;
  }

  /**
   * Retrieves the opened container of this player.
   *
   * @return The opened container of this player.
   */
  @Override
  public Object getPlayerContainer() {
    return Minecraft.getInstance().player.container;
  }

  /**
   * Retrieves the previous camera yaw of this player.
   *
   * @return The previous camera yaw of this player.
   */
  @Override
  public float getPrevCameraYaw() {
    return Minecraft.getInstance().player.prevCameraYaw;
  }

  /**
   * Retrieves the camera yaw of this player.
   *
   * @return The camera yaw of this player.
   */
  @Override
  public float getCameraYaw() {
    return Minecraft.getInstance().player.cameraYaw;
  }

  /**
   * Retrieves the previous chasing position X-axis of this player.
   *
   * @return The previous chasing position X-axis  of this player.
   */
  @Override
  public double getPrevChasingPosX() {
    return Minecraft.getInstance().player.prevChasingPosX;
  }

  /**
   * Retrieves the previous chasing position Y-axis of this player.
   *
   * @return The previous chasing position Y-axis  of this player.
   */
  @Override
  public double getPrevChasingPosY() {
    return Minecraft.getInstance().player.prevChasingPosY;
  }

  /**
   * Retrieves the previous chasing position Z-axis of this player.
   *
   * @return The previous chasing position Z-axis  of this player.
   */
  @Override
  public double getPrevChasingPosZ() {
    return Minecraft.getInstance().player.prevChasingPosZ;
  }

  /**
   * Retrieves the chasing position X-axis of this player.
   *
   * @return The chasing position X-axis  of this player.
   */
  @Override
  public double getChasingPosX() {
    return Minecraft.getInstance().player.chasingPosX;
  }

  /**
   * Retrieves the chasing position Y-axis of this player.
   *
   * @return The chasing position Y-axis  of this player.
   */
  @Override
  public double getChasingPosY() {
    return Minecraft.getInstance().player.chasingPosY;
  }

  /**
   * Retrieves the chasing position Z-axis of this player.
   *
   * @return The chasing position Z-axis  of this player.
   */
  @Override
  public double getChasingPosZ() {
    return Minecraft.getInstance().player.chasingPosZ;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PlayerSkinProfile getSkinProfile() {
    return this.getNetworkPlayerInfo().getSkinProfile();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void invalidate() {
    this.networkPlayerInfo = null;
  }

  /**
   * Removes the item from the cooldown tracking.
   *
   * @param item The item to be removed
   */
  @Override
  public void removeCooldown(Object item) {
    Minecraft.getInstance().player.getCooldownTracker().removeCooldown((Item) item);
  }
}
