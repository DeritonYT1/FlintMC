package net.labyfy.internal.component.player;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.PlayerSkinProfile;
import net.labyfy.component.player.util.SkinModel;
import net.labyfy.component.resources.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Default implementation of {@link PlayerSkinProfile}
 */
@Implement(PlayerSkinProfile.class)
public class DefaultPlayerSkinProfile implements PlayerSkinProfile {

  private final SkinModel skinModel;
  private final ResourceLocation skinLocation;
  private final ResourceLocation cloakLocation;
  private final ResourceLocation elytraLocation;

  @AssistedInject
  private DefaultPlayerSkinProfile(
          @Assisted("skinModel") SkinModel skinModel,
          @Assisted("skinLocation") @Nullable ResourceLocation skinLocation,
          @Assisted("cloakLocation") @Nullable ResourceLocation cloakLocation,
          @Assisted("elytraLocation") @Nullable ResourceLocation elytraLocation
  ) {
    this.skinModel = skinModel;
    this.skinLocation = skinLocation;
    this.cloakLocation = cloakLocation;
    this.elytraLocation = elytraLocation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SkinModel getSkinModel() {
    return this.skinModel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getSkinLocation() {
    return this.skinLocation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getCloakLocation() {
    return this.cloakLocation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getElytraLocation() {
    return this.elytraLocation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasSkin() {
    return this.skinLocation != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasCloak() {
    return this.cloakLocation != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasElytra() {
    return this.elytraLocation != null;
  }
}
