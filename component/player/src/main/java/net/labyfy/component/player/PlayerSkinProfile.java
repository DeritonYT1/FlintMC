package net.labyfy.component.player;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.player.util.SkinModel;
import net.labyfy.component.resources.ResourceLocation;

/**
 * Represents all skin-related things of a player.
 */
public interface PlayerSkinProfile {

  /**
   * Retrieves the skin model of this player
   *
   * @return The skin model of this player
   */
  SkinModel getSkinModel();

  /**
   * Retrieves the location of the player's skin
   *
   * @return The skin location
   */
  ResourceLocation getSkinLocation();

  /**
   * Retrieves the location of the player's cloak
   *
   * @return The cloak location
   */
  ResourceLocation getCloakLocation();

  /**
   * Retrieves the location of the player's elytra
   *
   * @return The elytra location
   */
  ResourceLocation getElytraLocation();

  /**
   * Whether the player has a skin.
   *
   * @return {@code true} if this player has a skin, otherwise {@code false}
   */
  boolean hasSkin();

  /**
   * Whether the player has a cloak.
   *
   * @return {@code true} if this player has a skin, otherwise {@code false}
   */
  boolean hasCloak();

  /**
   * Whether the player has a elytra.
   *
   * @return {@code true} if this player has a skin, otherwise {@code false}
   */
  boolean hasElytra();

  /**
   * A factory class for {@link PlayerSkinProfile}
   */
  @AssistedFactory(PlayerSkinProfile.class)
  interface Factory {

    /**
     * Creates a new {@link PlayerSkinProfile} with the given parameters.
     *
     * @param skinModel      The skin model of this profile.
     * @param skinLocation   The location of the skin.
     * @param cloakLocation  The location of the cloak.
     * @param elytraLocation The location of the elytra.
     * @return A created {@link PlayerSkinProfile}
     */
    PlayerSkinProfile create(
            @Assisted("skinModel") SkinModel skinModel,
            @Assisted("skinLocation") ResourceLocation skinLocation,
            @Assisted("cloakLocation") ResourceLocation cloakLocation,
            @Assisted("elytraLocation") ResourceLocation elytraLocation
    );

  }

}
