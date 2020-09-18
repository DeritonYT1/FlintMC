package net.labyfy.component.player.network;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.player.Player;
import net.labyfy.component.player.PlayerSkinProfile;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.serializer.util.GameModeSerializer;
import net.labyfy.component.player.serializer.util.SkinModelSerializer;
import net.labyfy.component.player.util.GameMode;
import net.labyfy.component.resources.ResourceLocationProvider;

/**
 * Represents the network information of a player.
 */
public interface NetworkPlayerInfo {

  /**
   * Retrieves the game profile form the network information.
   *
   * @return The game profile of a player
   */
  GameProfile getGameProfile();

  /**
   * Retrieves the response time from the network information.
   *
   * @return The response time form the network information
   */
  int getResponseTime();

  /**
   * Retrieves the player game mode from the network information.
   *
   * @return The player game mode
   */
  GameMode getGameMode();

  /**
   * Retrieves the player last health.
   *
   * @return The player last health
   */
  int getLastHealth();

  /**
   * Retrieves the player display health.
   *
   * @return The player display health
   */
  int getDisplayHealth();

  /**
   * Retrieves the player last health time.
   *
   * @return The player last health time
   */
  long getLastHealthTime();

  /**
   * Retrieves the player health blink time.
   *
   * @return The player health blink time
   */
  long getHealthBlinkTime();

  /**
   * Retrieves the player render visibility identifier.
   *
   * @return The player render visibility identifier
   */
  long getRenderVisibilityId();

  /**
   * Retrieves the player skin profile.
   *
   * @return The player's skin profile.
   */
  PlayerSkinProfile getSkinProfile();

  /**
   * A factory class for {@link NetworkPlayerInfo}
   */
  @AssistedFactory(NetworkPlayerInfo.class)
  interface Factory {
    /**
     * Creates a new {@link NetworkPlayerInfo} with the given parameters.
     *
     * @param player                   The player for this profile.
     * @param gameModeSerializer       A game mode serializer to serialize/deserialize the given mode.
     * @param resourceLocationProvider A provider to provide resource locations.
     * @param skinModelSerializer      A skin model serializer to serialize/deserialize the given skin model.
     * @param playerSkinProfileFactory A factory to create a player skin profile.
     * @return A created network player info
     */
    NetworkPlayerInfo create(
            @Assisted("player") Player player,
            @Assisted("gameModeSerializer") GameModeSerializer gameModeSerializer,
            @Assisted("resourceLocationProvider") ResourceLocationProvider resourceLocationProvider,
            @Assisted("skinModelSerializer") SkinModelSerializer skinModelSerializer,
            @Assisted("playerSkinProfileFactory") PlayerSkinProfile.Factory playerSkinProfileFactory
    );

  }

  /**
   * Represents a service interface for creating {@link NetworkPlayerInfo}
   */
  interface Provider {

    /**
     * Creates a new {@link NetworkPlayerInfo} with the given player.
     *
     * @param player The player for this profile.
     * @return A created network player info
     * @see Factory#create(Player, GameModeSerializer,
     * ResourceLocationProvider, SkinModelSerializer, PlayerSkinProfile.Factory)
     */
    NetworkPlayerInfo get(Player player);

  }

}
