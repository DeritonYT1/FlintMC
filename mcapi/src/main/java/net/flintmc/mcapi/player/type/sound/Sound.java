package net.flintmc.mcapi.player.type.sound;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.resources.ResourceLocation;

/** Represents a Minecraft sound. */
public interface Sound {

  /**
   * Retrieves the resource location of this sound.
   *
   * @return The resource location of this sound.
   */
  ResourceLocation getName();

  /** A factory class for {@link Sound} */
  @AssistedFactory(Sound.class)
  interface Factory {

    /**
     * Creates a new {@link Sound} with the given path.
     *
     * @param path The path of the sound.
     * @return The created sound.
     */
    Sound create(@Assisted("path") String path);
  }
}