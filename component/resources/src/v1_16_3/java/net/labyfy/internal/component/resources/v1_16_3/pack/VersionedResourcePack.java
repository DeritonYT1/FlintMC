package net.labyfy.internal.component.resources.v1_16_3.pack;

import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.pack.ResourcePack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * 1.16.3 implementation of a labyfy {@link ResourcePack}
 */
public class VersionedResourcePack implements ResourcePack {

  private final ResourcePackInfo info;

  /**
   * Constructs a new {@link VersionedResourcePack} based on the given info.
   * @param info Information about the resource pack
   */
  VersionedResourcePack(ResourcePackInfo info) {
    this.info = info;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getNameSpaces() {
    return this.info.getResourcePack().getResourceNamespaces(ResourcePackType.CLIENT_RESOURCES);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.info.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InputStream getStream(ResourceLocation resourceLocation) throws IOException {
    return this.info
            .getResourcePack()
            .getResourceStream(ResourcePackType.CLIENT_RESOURCES, resourceLocation.getHandle());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription() {
    return this.info.getDescription().getString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTitle() {
    return this.info.getTitle().getString();
  }
}
