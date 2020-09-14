package net.labyfy.internal.component.player.v1_16_3.network;

import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.Player;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.serializer.util.GameModeSerializer;
import net.labyfy.component.player.serializer.util.SkinModelSerializer;
import net.labyfy.component.player.util.GameMode;
import net.labyfy.component.player.util.SkinModel;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.world.GameType;

import java.util.UUID;

/**
 * 1.16.3 implementation of the {@link NetworkPlayerInfo}
 */
@Implement(value = NetworkPlayerInfo.class, version = "1.16.3")
public class VersionedNetworkPlayerInfo implements NetworkPlayerInfo {

  private final Player.Factory player;
  private final GameModeSerializer<GameType> gameModeSerializer;
  private final ResourceLocationProvider resourceLocationProvider;
  private final SkinModelSerializer<String> skinModelSerializer;

  @Inject
  public VersionedNetworkPlayerInfo(
          Player.Factory player,
          GameModeSerializer gameModeSerializer,
          ResourceLocationProvider provider,
          SkinModelSerializer skinModelSerializer
  ) {
    this.player = player;
    this.gameModeSerializer = gameModeSerializer;
    this.resourceLocationProvider = provider;
    this.skinModelSerializer = skinModelSerializer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameProfile getGameProfile() {
    return this.get().getGameProfile();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getResponseTime() {
    return this.getPlayerInfo(this.get().getUniqueId()).getResponseTime();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public GameMode getGameMode() {
    return this.gameModeSerializer.deserialize(
            this.getPlayerInfo(
                    this.get().getUniqueId()
            ).getGameType()
    );
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int getLastHealth() {
    return this.getPlayerInfo(this.get().getUniqueId()).getLastHealth();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int getDisplayHealth() {
    return this.getPlayerInfo(this.get().getUniqueId()).getDisplayHealth();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public long getLastHealthTime() {
    return this.getPlayerInfo(this.get().getUniqueId()).getLastHealthTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getHealthBlinkTime() {
    return this.getPlayerInfo(this.get().getUniqueId()).getHealthBlinkTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getRenderVisibilityId() {
    return this.getPlayerInfo(this.get().getUniqueId()).getRenderVisibilityId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SkinModel getSkinModel() {
    return this.skinModelSerializer.deserialize(
            this.getPlayerInfo(
                    this.get().getUniqueId()
            ).getSkinType()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getSkinLocation() {
    return this.resourceLocationProvider.get(
            this.getPlayerInfo(
                    this.get().getUniqueId()
            ).getLocationSkin().getPath()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getCloakLocation() {
    return this.resourceLocationProvider.get(
            this.getPlayerInfo(
                    this.get().getUniqueId()
            ).getLocationCape().getPath()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getElytraLocation() {
    return this.resourceLocationProvider.get(
            this.getPlayerInfo(
                    this.get().getUniqueId()
            ).getLocationElytra().getPath()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasSkin() {
    return this.getSkinLocation() != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasCloak() {
    return this.getCloakLocation() != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasElytra() {
    return this.getElytraLocation() != null;
  }

  /**
   * Retrieves a {@link net.minecraft.client.network.play.NetworkPlayerInfo} with the given unique identifier.
   *
   * @param uniqueId The unique identifier of the profile
   * @return A {@link net.minecraft.client.network.play.NetworkPlayerInfo} or {@code null}
   */
  private net.minecraft.client.network.play.NetworkPlayerInfo getPlayerInfo(UUID uniqueId) {
    return Minecraft.getInstance().getConnection().getPlayerInfo(uniqueId);
  }

  /**
   * Retrieves the player of this client.
   *
   * @return The client player
   */
  private Player get() {
    return this.player.create(Minecraft.getInstance().player);
  }

}
