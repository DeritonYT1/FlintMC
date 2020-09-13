package net.labyfy.internal.component.resources.v1_16_3;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStream;

/**
 * 1.16.3 implementation of a minecraft resource location.
 */
@Implement(value = ResourceLocation.class, version = "1.16.3")
public class VersionedResourceLocation extends net.minecraft.util.ResourceLocation implements ResourceLocation {

  @AssistedInject
  private VersionedResourceLocation(@Assisted("fullPath") String fullPath) {
    super(fullPath);
  }

  @AssistedInject
  private VersionedResourceLocation(
          @Assisted("nameSpace") String nameSpace,
          @Assisted("path") String path
  ) {
    super(nameSpace, path);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> T getHandle() {
    return (T) this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InputStream openInputStream() throws IOException {
    return Minecraft.getInstance().getResourceManager().getResource(this).getInputStream();
  }
}