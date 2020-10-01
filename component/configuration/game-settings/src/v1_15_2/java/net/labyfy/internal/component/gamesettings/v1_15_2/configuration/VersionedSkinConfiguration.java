package net.labyfy.internal.component.gamesettings.v1_15_2.configuration;

import com.google.inject.Singleton;
import net.labyfy.component.gamesettings.configuration.SkinConfiguration;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.player.util.PlayerClothing;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.HandSide;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 1.15.2 implementation of {@link SkinConfiguration}.
 */
@Singleton
@Implement(value = SkinConfiguration.class, version = "1.15.2")
public class VersionedSkinConfiguration implements SkinConfiguration {

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<PlayerClothing> getPlayerClothing() {
    return Minecraft.getInstance().gameSettings
            .getModelParts()
            .stream()
            .map(this::fromMinecraftObject)
            .collect(Collectors.toSet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setModelClothingEnabled(PlayerClothing clothing, boolean enable) {
    Minecraft.getInstance().gameSettings.setModelPartEnabled(this.toMinecraftObject(clothing), enable);
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void switchModelClothingEnabled(PlayerClothing clothing) {
    Minecraft.getInstance().gameSettings.switchModelPartEnabled(this.toMinecraftObject(clothing));
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Hand.Side getMainHand() {
    switch (Minecraft.getInstance().gameSettings.mainHand) {
      case LEFT:
        return Hand.Side.LEFT;
      case RIGHT:
        return Hand.Side.RIGHT;
      default:
        throw new IllegalStateException("Unexpected value: " + Minecraft.getInstance().gameSettings.mainHand);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMainHand(Hand.Side mainHand) {
    switch (mainHand) {
      case LEFT:
        Minecraft.getInstance().gameSettings.mainHand = HandSide.LEFT;
        Minecraft.getInstance().gameSettings.saveOptions();
        break;
      case RIGHT:
        Minecraft.getInstance().gameSettings.mainHand = HandSide.RIGHT;
        Minecraft.getInstance().gameSettings.saveOptions();
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + mainHand);
    }
  }

  private PlayerClothing fromMinecraftObject(PlayerModelPart playerModelPart) {
    switch (playerModelPart) {
      case CAPE:
        return PlayerClothing.CLOAK;
      case JACKET:
        return PlayerClothing.JACKET;
      case LEFT_SLEEVE:
        return PlayerClothing.LEFT_SLEEVE;
      case RIGHT_SLEEVE:
        return PlayerClothing.RIGHT_SLEEVE;
      case LEFT_PANTS_LEG:
        return PlayerClothing.LEFT_PANTS_LEG;
      case RIGHT_PANTS_LEG:
        return PlayerClothing.RIGHT_PANTS_LEG;
      case HAT:
        return PlayerClothing.HAT;
      default:
        throw new IllegalStateException("Unexpected value: " + playerModelPart);
    }
  }

  private PlayerModelPart toMinecraftObject(PlayerClothing clothing) {
    switch (clothing) {
      case CLOAK:
        return PlayerModelPart.CAPE;
      case JACKET:
        return PlayerModelPart.JACKET;
      case LEFT_SLEEVE:
        return PlayerModelPart.LEFT_SLEEVE;
      case RIGHT_SLEEVE:
        return PlayerModelPart.RIGHT_SLEEVE;
      case LEFT_PANTS_LEG:
        return PlayerModelPart.LEFT_PANTS_LEG;
      case RIGHT_PANTS_LEG:
        return PlayerModelPart.RIGHT_PANTS_LEG;
      case HAT:
        return PlayerModelPart.HAT;
      default:
        throw new IllegalStateException("Unexpected value: " + clothing);
    }
  }
}
