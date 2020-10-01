package net.labyfy.internal.component.player.v1_16_3;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.authlib.GameProfile;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.RemoteClientPlayer;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.util.*;
import net.labyfy.component.player.serializer.util.sound.SoundCategorySerializer;
import net.labyfy.component.player.serializer.util.sound.SoundSerializer;
import net.labyfy.component.world.ClientWorld;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.GameType;

/**
 * 1.16.3 implementation of {@link RemoteClientPlayer.Provider}
 */
@Singleton
@Implement(value = RemoteClientPlayer.Provider.class, version = "1.16.3")
public class VersionedRemoteClientPlayerProvider implements RemoteClientPlayer.Provider {

  private final RemoteClientPlayer.Factory remoteClientPlayerFactory;
  private final HandSerializer<Hand> handSerializer;
  private final HandSideSerializer<HandSide> handSideSerializer;
  private final GameModeSerializer<GameType> gameModeSerializer;
  private final GameProfileSerializer<GameProfile> gameProfileSerializer;
  private final MinecraftComponentMapper minecraftComponentMapper;
  private final NetworkPlayerInfo.Provider networkPlayerInfoProvider;
  private final PlayerClothingSerializer<PlayerModelPart> playerClothingSerializer;
  private final PoseSerializer<Pose> poseSerializer;
  private final SoundCategorySerializer<SoundCategory> soundCategorySerializer;
  private final SoundSerializer<SoundEvent> soundSerializer;

  private final ClientWorld clientWorld;

  @Inject
  public VersionedRemoteClientPlayerProvider(
          RemoteClientPlayer.Factory remoteClientPlayerFactory,
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
          ClientWorld clientWorld) {
    this.remoteClientPlayerFactory = remoteClientPlayerFactory;
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
    this.clientWorld = clientWorld;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteClientPlayer get(Object player) {
    return this.remoteClientPlayerFactory.create(
            player,
            this.handSerializer,
            this.handSideSerializer,
            this.gameModeSerializer,
            this.gameProfileSerializer,
            this.minecraftComponentMapper,
            this.networkPlayerInfoProvider,
            this.playerClothingSerializer,
            this.poseSerializer,
            this.soundCategorySerializer,
            this.soundSerializer,
            this.clientWorld
    );
  }
}
