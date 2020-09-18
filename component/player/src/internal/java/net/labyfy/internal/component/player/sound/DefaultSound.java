package net.labyfy.internal.component.player.sound;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.resources.ResourceLocation;

/**
 * Default implementation of {@link Sound}
 */
@Implement(Sound.class)
public class DefaultSound implements Sound {

  private final ResourceLocation name;

  @AssistedInject
  private DefaultSound(@Assisted("path") ResourceLocation path) {
    this.name = path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getName() {
    return this.name;
  }
}
