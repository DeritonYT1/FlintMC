package net.labyfy.internal.component.player.v1_16_3;

import com.google.inject.Inject;
import io.netty.buffer.Unpooled;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.ClientPlayer;
import net.labyfy.component.player.overlay.TabOverlay;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.util.*;
import net.labyfy.component.player.serializer.util.sound.SoundCategorySerializer;
import net.labyfy.component.player.serializer.util.sound.SoundSerializer;
import net.labyfy.component.player.util.Hand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CChatMessagePacket;
import net.minecraft.network.play.client.CCustomPayloadPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

/**
 * 1.16.3 implementation of a minecraft player.
 */
@Implement(value = ClientPlayer.class, version = "1.16.3")
public class VersionedClientPlayer extends VersionedPlayer implements ClientPlayer<AbstractClientPlayerEntity> {


  private ClientPlayerEntity player;

  @Inject
  protected VersionedClientPlayer(
          HandSerializer handSerializer,
          HandSideSerializer handSideSerializer,
          GameModeSerializer gameModeSerializer,
          GameProfileSerializer gameProfileSerializer,
          MinecraftComponentMapper minecraftComponentMapper,
          PlayerClothingSerializer playerClothingSerializer,
          PoseSerializer poseSerializer,
          SoundCategorySerializer soundCategorySerializer,
          SoundSerializer soundSerializer
  ) {
    super(
            handSerializer,
            handSideSerializer,
            gameModeSerializer,
            gameProfileSerializer,
            minecraftComponentMapper,
            playerClothingSerializer,
            poseSerializer,
            soundCategorySerializer,
            soundSerializer
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPlayer(AbstractClientPlayerEntity player) {
    super.setPlayer(player);
    this.player = (ClientPlayerEntity) player;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClientPlayerEntity getPlayer() {
    return player;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getPlayerInventory() {
    return this.player.inventory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Object> getArmorContents() {
    return Collections.singletonList(this.player.inventory.armorInventory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getExperienceLevel() {
    return this.player.experienceLevel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getExperienceTotal() {
    return this.player.experienceTotal;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getExperience() {
    return this.player.experience;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLocale() {
    return Minecraft.getInstance().getLanguageManager().getCurrentLanguage().getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TabOverlay getTabOverlay() {
    return InjectionHolder.getInjectedInstance(TabOverlay.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasPlayerInfo() {
    return this.getNetworkPlayerInfo() != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getFovModifier() {
    return this.player.getFovModifier();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAutoJumpEnabled() {
    return this.player.isAutoJumpEnabled();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRidingHorse() {
    return this.player.isRidingHorse();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRowingBoat() {
    return this.player.isRowingBoat();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShowDeathScreen() {
    return this.player.isShowDeathScreen();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getServerBrand() {
    return this.player.getServerBrand();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setServerBrand(String brand) {
    this.player.setServerBrand(brand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getWaterBrightness() {
    return this.player.getWaterBrightness();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPrevTimeInPortal() {
    return this.player.prevTimeInPortal;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getTimeInPortal() {
    return this.player.timeInPortal;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendMessage(String content) {
    this.player.sendChatMessage(content);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean performCommand(String command) {
    if (!command.startsWith("/")) return false;

    Minecraft.getInstance().getConnection().sendPacket(new CChatMessagePacket(command));

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendMessage(ChatComponent component) {
    this.player.sendMessage(
            (ITextComponent) this.minecraftComponentMapper.toMinecraft(component),
            getPlayerUniqueId()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean drop(boolean dropEntireStack) {
    return this.player.drop(dropEntireStack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void swingArm(Hand hand) {
    this.player.swingArm(this.handSerializer.serialize(hand));
  }

  @Override
  public void respawnPlayer() {
    this.player.respawnPlayer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void closeScreenAndDropStack() {
    this.player.closeScreenAndDropStack();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canSwim() {
    return this.player.canSwim();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendHorseInventory() {
    this.player.sendHorseInventory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getHorseJumpPower() {
    return this.player.getHorseJumpPower();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setExperienceStats(int currentExperience, int maxExperience, int level) {
    this.player.setXPStats(currentExperience, maxExperience, level);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHoldingSneakKey() {
    return this.player.func_228354_I_();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getStatistics() {
    return this.player.getStats();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getRecipeBook() {
    return this.player.getRecipeBook();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeRecipeHighlight(Object recipe) {
    this.player.removeRecipeHighlight((IRecipe<?>) recipe);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePlayerHealth(float health) {
    this.player.setPlayerSPHealth(health);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateEntityActionState() {
    this.player.updateEntityActionState();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendPluginMessage(String channel, byte[] data) {
    this.player.connection.sendPacket(
            new CCustomPayloadPacket(
                    new ResourceLocation(channel),
                    new PacketBuffer(Unpooled.wrappedBuffer(data))
            )
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getConnection() {
    return this.player.connection;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getBiome() {
    World world = (World) this.getWorld().getClientWorld();
    Entity renderViewEntity = Minecraft.getInstance().getRenderViewEntity();
    if (renderViewEntity == null) return null;
    BlockPos blockPos = new BlockPos(renderViewEntity.getPositionVec());

    if (world != null) {
      String biomePath = world.func_241828_r().func_243612_b(Registry.BIOME_KEY).getKey(world.getBiome(blockPos)).getPath();
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
   * {@inheritDoc}
   */
  @Override
  public void sendStatusMessage(ChatComponent component, boolean actionBar) {
    this.player.sendStatusMessage(
            (ITextComponent) this.minecraftComponentMapper.toMinecraft(component),
            actionBar
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openSignEditor(Object signTileEntity) {
    this.player.openSignEditor((SignTileEntity) signTileEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openMinecartCommandBlock(Object commandBlock) {
    this.player.openMinecartCommandBlock((CommandBlockLogic) commandBlock);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openCommandBlock(Object commandBlock) {
    this.player.openCommandBlock((CommandBlockTileEntity) commandBlock);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openStructureBlock(Object structureBlock) {
    this.player.openStructureBlock((StructureBlockTileEntity) structureBlock);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openJigsaw(Object jigsaw) {
    this.player.openJigsaw((JigsawTileEntity) jigsaw);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openHorseInventory(Object horse, Object inventory) {
    this.player.openHorseInventory((AbstractHorseEntity) horse, (IInventory) inventory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openMerchantInventory(Object merchantOffers, int container, int levelProgress, int experience, boolean regularVillager, boolean refreshable) {
    super.openMerchantInventory(merchantOffers, container, levelProgress, experience, regularVillager, refreshable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openBook(Object itemStack, Hand hand) {
    this.player.openBook((ItemStack) itemStack, this.handSerializer.serialize(hand));
  }
}
