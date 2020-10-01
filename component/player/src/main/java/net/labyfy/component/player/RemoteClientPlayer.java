package net.labyfy.component.player;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.util.*;
import net.labyfy.component.player.serializer.util.sound.SoundCategorySerializer;
import net.labyfy.component.player.serializer.util.sound.SoundSerializer;
import net.labyfy.component.world.ClientWorld;

/**
 * Represents a remote client player
 */
public interface RemoteClientPlayer extends Player {

  /**
   * A factory class for {@link RemoteClientPlayer}
   */
  @AssistedFactory(RemoteClientPlayer.class)
  interface Factory {

    /**
     * Creates a new {@link RemoteClientPlayer} with the given type.
     *
     * @param player                    The player for the remote client.
     * @param handSerializer            A hand serializer to serialize/deserialize the hands.
     * @param handSideSerializer        A hand side serializer to serialize/deserialize the hand sides.
     * @param gameModeSerializer        A game mode serializer to serialize/deserialize the game modes.
     * @param gameProfileSerializer     A game profile serializer to serialize/deserialize profiles.
     * @param minecraftComponentMapper  A minecraft component mapper
     * @param networkPlayerInfoProvider A provider to provides the network player infos.
     * @param playerClothingSerializer  A player clothing serializer to serialize/deserialize clothing.
     * @param poseSerializer            A pose serializer to serialize/deserialize poses.
     * @param soundCategorySerializer   A sound category serializer to serialize/deserialize sound categories.
     * @param soundSerializer           A sound serializer to serialize/deserialize sounds.
     * @return A created remote client player
     */
    RemoteClientPlayer create(
            @Assisted("player") Object player,
            @Assisted("handSerializer") HandSerializer handSerializer,
            @Assisted("handSideSerializer") HandSideSerializer handSideSerializer,
            @Assisted("gameModeSerializer") GameModeSerializer gameModeSerializer,
            @Assisted("gameProfileSerializer") GameProfileSerializer gameProfileSerializer,
            @Assisted("minecraftComponentMapper") MinecraftComponentMapper minecraftComponentMapper,
            @Assisted("networkPlayerInfoProvider") NetworkPlayerInfo.Provider networkPlayerInfoProvider,
            @Assisted("playerClothingSerializer") PlayerClothingSerializer playerClothingSerializer,
            @Assisted("poseSerializer") PoseSerializer poseSerializer,
            @Assisted("soundCategorySerializer") SoundCategorySerializer soundCategorySerializer,
            @Assisted("soundSerializer") SoundSerializer soundSerializer,
            @Assisted("world") ClientWorld clientWorld
    );

  }

  /**
   * Represents a service interface for creating {@link RemoteClientPlayer}
   */
  interface Provider {

    /**
     * Creates a new {@link RemoteClientPlayer} with the given player.
     *
     * @param player The player for the remote client.
     * @return The created remote client player.
     */
    RemoteClientPlayer get(Object player);

  }

}
