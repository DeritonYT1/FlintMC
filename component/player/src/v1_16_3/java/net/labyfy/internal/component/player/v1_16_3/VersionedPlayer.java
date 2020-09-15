package net.labyfy.internal.component.player.v1_16_3;

import com.google.inject.Inject;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.Player;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.util.*;
import net.labyfy.component.player.serializer.util.sound.SoundCategorySerializer;
import net.labyfy.component.player.serializer.util.sound.SoundSerializer;
import net.labyfy.component.player.util.*;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.player.util.sound.SoundCategory;
import net.labyfy.component.player.world.ClientWorld;
import net.labyfy.component.resources.ResourceLocation;
import net.minecraft.block.BlockState;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.*;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.UUID;

/**
 * 1.16.3 implementation of a {@link Player}
 */
@Implement(value = Player.class, version = "1.16.3")
public class VersionedPlayer implements Player<AbstractClientPlayerEntity> {

  protected final HandSerializer<net.minecraft.util.Hand> handSerializer;
  protected final HandSideSerializer<HandSide> handSideSerializer;
  protected final GameModeSerializer<GameType> gameModeSerializer;
  protected final GameProfileSerializer<com.mojang.authlib.GameProfile> gameProfileSerializer;
  protected final MinecraftComponentMapper minecraftComponentMapper;
  protected final PlayerClothingSerializer<PlayerModelPart> playerClothingSerializer;
  protected final PoseSerializer<Pose> poseSerializer;
  protected final SoundCategorySerializer<net.minecraft.util.SoundCategory> soundCategorySerializer;
  protected final SoundSerializer<SoundEvent> soundSerializer;

  private AbstractClientPlayerEntity player;

  @Inject
  protected VersionedPlayer(
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
    this.handSerializer = handSerializer;
    this.handSideSerializer = handSideSerializer;
    this.gameModeSerializer = gameModeSerializer;
    this.gameProfileSerializer = gameProfileSerializer;
    this.minecraftComponentMapper = minecraftComponentMapper;
    this.playerClothingSerializer = playerClothingSerializer;
    this.poseSerializer = poseSerializer;
    this.soundCategorySerializer = soundCategorySerializer;
    this.soundSerializer = soundSerializer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPlayer(AbstractClientPlayerEntity player) {
    this.player = player;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractClientPlayerEntity getPlayer() {
    return this.player;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClientWorld getWorld() {
    return InjectionHolder.getInjectedInstance(ClientWorld.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameProfile getGameProfile() {
    return this.gameProfileSerializer.deserialize(this.player.getGameProfile());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getName() {
    return this.minecraftComponentMapper.fromMinecraft(this.player.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDisplayName() {
    return this.minecraftComponentMapper.fromMinecraft(this.player.getDisplayName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDisplayNameAndUniqueId() {
    return this.minecraftComponentMapper.fromMinecraft(
            (new StringTextComponent(""))
                    .append(this.player.getName())
                    .appendString(" (")
                    .appendString(this.getPlayerUniqueId().toString())
                    .appendString(")")
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getUniqueId() {
    return this.player.getUniqueID();
  }

  @Override
  public UUID getPlayerUniqueId() {
    return this.getGameProfile().getUniqueId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getHealth() {
    return this.player.getHealth();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPlayerListName() {
    return this.player.getScoreboardName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getPlayerTime() {
    return this.getWorld().getTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getX() {
    return this.player.getPosX();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getY() {
    return this.player.getPosY();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getZ() {
    return this.player.getPosZ();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPitch() {
    return this.player.getPitchYaw().x;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPitch(float partialTicks) {
    return this.player.getPitch(partialTicks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getYaw() {
    return this.player.getPitchYaw().y;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getYaw(float partialTicks) {
    return this.player.getYaw(partialTicks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getRotationYawHead() {
    return this.player.getRotationYawHead();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getEyeHeight() {
    return this.player.getEyeHeight();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getEyeHeight(EntityPose entityPose) {
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPosYEye() {
    return this.player.getPosYEye();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityPose getPose() {
    return this.poseSerializer.deserialize(this.player.getPose());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSpectator() {
    return this.player.isSpectator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCreative() {
    return this.player.isCreative();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Hand getActiveHand() {
    return this.handSerializer.deserialize(this.player.getActiveHand());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHandActive() {
    return this.player.isHandActive();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getActiveItemStack() {
    return this.player.getActiveItemStack();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getItemInUseCount() {
    return this.player.getItemInUseCount();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getItemInUseMaxCount() {
    return this.player.getItemInUseMaxCount();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFoodLevel() {
    return this.player.getFoodStats().getFoodLevel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getSaturationLevel() {
    return this.player.getFoodStats().getSaturationLevel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean needFood() {
    return this.player.getFoodStats().needFood();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRangeToRender3d(double x, double y, double z) {
    return this.player.isInRangeToRender3d(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInRangeToRender(double distance) {
    return this.player.isInRangeToRenderDist(distance);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAlive() {
    return this.player.isAlive();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSprinting() {
    return this.player.isSprinting();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isOnLadder() {
    return this.player.isOnLadder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSleeping() {
    return this.player.isSleeping();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSwimming() {
    return this.player.isSwimming();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWet() {
    return this.player.isWet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCrouching() {
    return this.player.isCrouching();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isGlowing() {
    return this.player.isGlowing();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSneaking() {
    return this.player.isSneaking();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInWater() {
    return this.player.isInWater();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInLava() {
    return this.player.isInLava();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWearing(PlayerClothing playerClothing) {
    return this.player.isWearing(
            this.playerClothingSerializer.serialize(playerClothing)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInvisible() {
    return this.player.isInvisible();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInvisible(boolean invisible) {
    this.player.setInvisible(invisible);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBurning() {
    return this.player.isBurning();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isElytraFlying() {
    return this.player.isElytraFlying();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAbsorptionAmount(float amount) {
    this.player.setAbsorptionAmount(amount);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getAbsorptionAmount() {
    return this.player.getAbsorptionAmount();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTotalArmorValue() {
    return this.player.getTotalArmorValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendMessage(ChatComponent component) {
    this.player.sendMessage((ITextComponent) this.minecraftComponentMapper.toMinecraft(component), this.getPlayerUniqueId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NetworkPlayerInfo getNetworkPlayerInfo() {
    return InjectionHolder.getInjectedInstance(NetworkPlayerInfo.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void playSound(Sound sound, float volume, float pitch) {
    this.playSound(sound, this.getSoundCategory(), volume, pitch);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void playSound(Sound sound, SoundCategory category, float volume, float pitch) {
    this.player.playSound(
            this.soundSerializer.serialize(sound),
            this.soundCategorySerializer.serialize(category),
            volume,
            pitch
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getFallDistance() {
    return this.player.fallDistance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxFallDistance() {
    return this.player.getMaxFallHeight();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void swingArm(Hand hand) {
    this.player.swingArm(this.handSerializer.serialize(hand));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxInPortalTime() {
    return this.player.getMaxInPortalTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getFlySpeed() {
    return this.player.abilities.getFlySpeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFlySpeed(float speed) {
    this.player.abilities.setFlySpeed(speed);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getWalkSpeed() {
    return this.player.abilities.getWalkSpeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWalkSpeed(float speed) {
    this.player.abilities.setWalkSpeed(speed);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isOnGround() {
    return this.player.isOnGround();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getLuck() {
    return this.player.getLuck();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Hand.Side getPrimaryHand() {
    return this.handSideSerializer.deserialize(this.player.getPrimaryHand());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPrimaryHand(Hand.Side side) {
    this.player.setPrimaryHand(this.handSideSerializer.serialize(side));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canUseCommandBlock() {
    return this.player.canUseCommandBlock();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCooldownPeriod() {
    return this.player.getCooldownPeriod();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCooledAttackStrength(float adjustTicks) {
    return this.player.getCooledAttackStrength(adjustTicks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resetCooldown() {
    this.player.resetCooldown();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasReducedDebug() {
    return this.player.hasReducedDebug();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setReducedDebug(boolean reducedDebug) {
    this.player.setReducedDebug(reducedDebug);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean replaceItemInInventory(int slot, Object itemStack) {
    return this.player.replaceItemInInventory(slot, (ItemStack) itemStack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPushedByWater() {
    return this.player.isPushedByWater();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Object> getHeldEquipment() {
    return Collections.singleton(this.player.getHeldEquipment());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Object> getArmorInventoryList() {
    return Collections.singletonList(this.player.getArmorInventoryList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean addItemStackToInventory(Object itemStack) {
    return this.player.addItemStackToInventory((ItemStack) itemStack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAllowEdit() {
    return this.player.isAllowEdit();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean shouldHeal() {
    return this.player.shouldHeal();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canEat(boolean ignoreHunger) {
    return this.player.canEat(ignoreHunger);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addExhaustion(float exhaustion) {
    this.player.addExhaustion(exhaustion);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addExperienceLevel(int levels) {
    this.player.addExperienceLevel(levels);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int experienceBarCap() {
    return this.player.xpBarCap();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getExperienceSpeed() {
    return this.player.getXPSeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void giveExperiencePoints(int points) {
    this.player.giveExperiencePoints(points);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean tryToStartFallFlying() {
    return this.player.tryToStartFallFlying();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startFallFlying() {
    this.player.startFallFlying();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stopFallFlying() {
    this.player.stopFallFlying();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void jump() {
    this.player.jump();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateSwimming() {
    this.player.updateSwimming();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getAIMoveSpeed() {
    return this.player.getAIMoveSpeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addMovementStat(double x, double y, double z) {
    this.player.addMovementStat(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSpawnForced() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPlayerFullyAsleep() {
    return this.player.isPlayerFullyAsleep();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getSleepTimer() {
    return this.player.getSleepTimer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void wakeUp() {
    this.player.wakeUp();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void wakeUp(boolean updateTimer, boolean updateSleepingPlayers) {
    this.player.stopSleepInBed(updateTimer, updateSleepingPlayers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disableShield(boolean sprinting) {
    this.player.disableShield(sprinting);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stopRiding() {
    this.player.stopRiding();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canAttackPlayer(Player<AbstractClientPlayerEntity> player) {
    return this.player.canAttackPlayer((PlayerEntity) player);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getScore() {
    return this.player.getScore();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setScore(int score) {
    this.player.setScore(score);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addScore(int score) {
    this.player.addScore(score);
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
  public Object dropItem(Object droppedItem, boolean traceItem) {
    return this.player.dropItem((ItemStack) droppedItem, traceItem);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object dropItem(Object droppedItem, boolean dropAround, boolean traceItem) {
    return this.player.dropItem((ItemStack) droppedItem, dropAround, traceItem);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getDigSpeed(Object blockState) {
    return this.player.getDigSpeed((BlockState) blockState);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canHarvestBlock(Object blockState) {
    return this.player.func_234569_d_((BlockState) blockState);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(Object compoundNBT) {
    this.player.readAdditional((CompoundNBT) compoundNBT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(Object compoundNBT) {
    this.player.writeAdditional((CompoundNBT) compoundNBT);
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
  public Object findAmmo(Object shootable) {
    return this.player.findAmmo((ItemStack) shootable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canPickUpItem(Object itemStack) {
    return this.player.canPickUpItem((ItemStack) itemStack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean addShoulderEntity(Object compoundNbt) {
    return this.player.addShoulderEntity((CompoundNBT) compoundNbt);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getLeftShoulderEntity() {
    return this.player.getLeftShoulderEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getRightShoulderEntity() {
    return this.player.getRightShoulderEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFireTimer() {
    return this.player.getFireTimer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getStepHeight() {
    return this.player.stepHeight;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getRotateElytraX() {
    return this.player.rotateElytraX;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getRotateElytraY() {
    return this.player.rotateElytraY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getRotateElytraZ() {
    return this.player.rotateElytraZ;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCollided() {
    return this.isCollidedHorizontally() || this.isCollidedVertically();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCollidedHorizontally() {
    return this.player.collidedHorizontally;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCollidedVertically() {
    return this.player.collidedVertically;
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
  public void openMerchantInventory(
          Object merchantOffers,
          int container,
          int levelProgress,
          int experience,
          boolean regularVillager,
          boolean refreshable
  ) {
    this.player.openMerchantContainer(
            container,
            (MerchantOffers) merchantOffers,
            levelProgress,
            experience,
            regularVillager,
            refreshable
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openBook(Object itemStack, Hand hand) {
    this.player.openBook((ItemStack) itemStack, this.handSerializer.serialize(hand));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void preparePlayerToSpawn() {
    this.player.preparePlayerToSpawn();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean blockActionRestricted(ClientWorld clientWorld, Object blockPos, GameMode gameMode) {
    return this.player.blockActionRestricted(
            (World) this.getWorld().getClientWorld(),
            (BlockPos) blockPos,
            this.gameModeSerializer.serialize(gameMode)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void spawnSweepParticles() {
    this.player.spawnSweepParticles();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void respawnPlayer() {
    this.player.respawnPlayer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendPlayerAbilities() {
    this.player.sendPlayerAbilities();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateGameMode(GameMode gameMode) {
    this.player.setGameType(
            this.gameModeSerializer.serialize(gameMode)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void enchantItem(Object itemStack, int cost) {
    this.player.onEnchant((ItemStack) itemStack, cost);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getOpenedContainer() {
    return this.player.openContainer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getPlayerContainer() {
    return this.player.container;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPrevCameraYaw() {
    return this.player.prevCameraYaw;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCameraYaw() {
    return this.player.cameraYaw;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPrevChasingPosX() {
    return this.player.prevChasingPosX;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPrevChasingPosY() {
    return this.player.prevChasingPosY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPrevChasingPosZ() {
    return this.player.prevChasingPosZ;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getChasingPosX() {
    return this.player.chasingPosX;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getChasingPosY() {
    return this.player.chasingPosY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getChasingPosZ() {
    return this.player.chasingPosZ;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SkinModel getSkinModel() {
    return this.getNetworkPlayerInfo().getSkinModel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getSkinLocation() {
    return this.getNetworkPlayerInfo().getSkinLocation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getCloakLocation() {
    return this.getNetworkPlayerInfo().getCloakLocation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getElytraLocation() {
    return this.getNetworkPlayerInfo().getElytraLocation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasSkin() {
    return this.getNetworkPlayerInfo().hasSkin();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasCloak() {
    return this.getNetworkPlayerInfo().hasCloak();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasElytra() {
    return this.getNetworkPlayerInfo().hasElytra();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasCooldown(Object item) {
    return this.player.getCooldownTracker().hasCooldown((Item) item);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCooldown(Object item, float partialTicks) {
    return this.player.getCooldownTracker().getCooldown((Item) item, partialTicks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCooldown(Object item, int ticks) {
    this.player.getCooldownTracker().setCooldown((Item) item, ticks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeCooldown(Object item) {
    this.player.getCooldownTracker().removeCooldown((Item) item);
  }
}
