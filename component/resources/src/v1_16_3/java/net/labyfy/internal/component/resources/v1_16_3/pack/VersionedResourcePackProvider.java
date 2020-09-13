package net.labyfy.internal.component.resources.v1_16_3.pack;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.resources.pack.ResourcePack;
import net.labyfy.component.resources.pack.ResourcePackProvider;
import net.minecraft.client.Minecraft;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 1.16.3 implementation of the {@link ResourcePackProvider}
 */
@Implement(value = ResourcePackProvider.class, version = "1.16.3")
public class VersionedResourcePackProvider implements ResourcePackProvider {

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<ResourcePack> getEnabled() {
    return Minecraft.getInstance().getResourcePackList().getEnabledPacks().stream()
            .map(VersionedResourcePack::new)
            .collect(Collectors.toSet());
  }
}
