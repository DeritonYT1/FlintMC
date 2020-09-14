package net.labyfy.internal.component.player.v1_16_3;

import com.google.inject.Inject;
import com.mojang.authlib.GameProfile;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.RemoteClientPlayer;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.util.*;
import net.labyfy.component.player.serializer.util.sound.SoundCategorySerializer;
import net.labyfy.component.player.serializer.util.sound.SoundSerializer;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.GameType;

/**
 * 1.16.3 implementation of {@link RemoteClientPlayer}
 */
@Implement(value = RemoteClientPlayer.class, version = "1.16.3")
public class VersionedRemoteClientPlayer extends VersionedPlayer implements RemoteClientPlayer<AbstractClientPlayerEntity> {

  private RemoteClientPlayerEntity player;

  @Inject
  protected VersionedRemoteClientPlayer(
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
    this.player = (RemoteClientPlayerEntity) player;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteClientPlayerEntity getPlayer() {
    return player;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInRangeToRender(double distance) {
    return this.player.isInRangeToRenderDist(distance);
  }
}
