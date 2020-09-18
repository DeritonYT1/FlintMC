package net.labyfy.internal.component.player.v1_16_3.network;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.Player;
import net.labyfy.component.player.PlayerSkinProfile;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.serializer.util.GameModeSerializer;
import net.labyfy.component.player.serializer.util.SkinModelSerializer;
import net.labyfy.component.player.util.GameMode;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.world.GameType;

import java.util.UUID;

/**
 * 1.16.3 implementation of the {@link NetworkPlayerInfo}
 */
@Implement(value = NetworkPlayerInfo.class, version = "1.16.3")
public class VersionedNetworkPlayerInfo implements NetworkPlayerInfo {

  private final Player player;
  private final GameModeSerializer<GameType> gameModeSerializer;
  private final ResourceLocationProvider resourceLocationProvider;
  private final SkinModelSerializer<String> skinModelSerializer;
  private final PlayerSkinProfile.Factory playerSkinProfileFactory;

  private PlayerSkinProfile playerSkinProfile;

  @Inject
  public VersionedNetworkPlayerInfo(
          @Assisted("player") Player player,
          @Assisted("gameModeSerializer") GameModeSerializer gameModeSerializer,
          @Assisted("resourceLocationProvider") ResourceLocationProvider resourceLocationProvider,
          @Assisted("skinModelSerializer") SkinModelSerializer skinModelSerializer,
          @Assisted("playerSkinProfileFactory") PlayerSkinProfile.Factory playerSkinProfileFactory
  ) {
    this.player = player;
    this.gameModeSerializer = gameModeSerializer;
    this.resourceLocationProvider = resourceLocationProvider;
    this.skinModelSerializer = skinModelSerializer;
    this.playerSkinProfileFactory = playerSkinProfileFactory;
  }

  /**
   * Retrieves the game profile form the network information.
   *
   * @return The game profile of a player
   */
  @Override
  public GameProfile getGameProfile() {
    return this.player.getGameProfile();
  }

  /**
   * Retrieves the response time from the network information.
   *
   * @return The response time form the network information
   */
  @Override
  public int getResponseTime() {
    return this.getPlayerInfo(this.player.getUniqueId()).getResponseTime();
  }

  /**
   * Retrieves the player game mode from the network information.
   *
   * @return The player game mode
   */
  @Override
  public GameMode getGameMode() {
    return this.gameModeSerializer.deserialize(
            this.getPlayerInfo(
                    this.player.getUniqueId()
            ).getGameType()
    );
  }

  /**
   * Retrieves the player last health.
   *
   * @return The player last health
   */
  @Override
  public int getLastHealth() {
    return this.getPlayerInfo(this.player.getPlayerUniqueId()).getLastHealth();
  }

  /**
   * Retrieves the player display health.
   *
   * @return The player display health
   */
  @Override
  public int getDisplayHealth() {
    return this.getPlayerInfo(this.player.getPlayerUniqueId()).getDisplayHealth();
  }

  /**
   * Retrieves the player last health time.
   *
   * @return The player last health time
   */
  @Override
  public long getLastHealthTime() {
    return this.getPlayerInfo(this.player.getPlayerUniqueId()).getLastHealthTime();
  }

  /**
   * Retrieves the player health blink time.
   *
   * @return The player health blink time
   */
  @Override
  public long getHealthBlinkTime() {
    return this.getPlayerInfo(this.player.getPlayerUniqueId()).getHealthBlinkTime();
  }

  /**
   * Retrieves the player render visibility identifier.
   *
   * @return The player render visibility identifier
   */
  @Override
  public long getRenderVisibilityId() {
    return this.getPlayerInfo(this.player.getPlayerUniqueId()).getRenderVisibilityId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PlayerSkinProfile getSkinProfile() {
    if (this.playerSkinProfile == null) {
      net.minecraft.client.network.play.NetworkPlayerInfo playerInfo = this.getPlayerInfo(
              this.player.getPlayerUniqueId()
      );

      this.playerSkinProfile = this.playerSkinProfileFactory.create(
              this.skinModelSerializer.deserialize(playerInfo.getSkinType()),
              playerInfo.getLocationSkin() == null ?
                      null : this.resourceLocationProvider.get(playerInfo.getLocationSkin().getPath()),
              playerInfo.getLocationCape() == null ?
                      null : this.resourceLocationProvider.get(playerInfo.getLocationCape().getPath()),
              playerInfo.getLocationElytra() == null ?
                      null : this.resourceLocationProvider.get(playerInfo.getLocationElytra().getPath())
      );
    }

    return this.playerSkinProfile;
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

}
