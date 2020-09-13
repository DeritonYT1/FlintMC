package net.labyfy.internal.component.resources.v1_16_3;

import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 1.16.3 implementation of the {@link ResourceLocationProvider}
 */
@Implement(value = ResourceLocationProvider.class, version = "1.16.3")
public class VersionedResourceLocationProvider implements ResourceLocationProvider{

  private final VersionedResourceLocation.Factory resourceLocationFactory;

  @Inject
  private VersionedResourceLocationProvider(VersionedResourceLocation.Factory resourceLocationFactory) {
    this.resourceLocationFactory = resourceLocationFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation get(String path) {
    return this.get("minecraft", path);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation get(String nameSpace, String path) {
    return this.resourceLocationFactory.create(nameSpace, path);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<ResourceLocation> getRecursive(ResourceLocation resourceLocation) throws IOException {
    return Minecraft.getInstance().getResourceManager()
            .getAllResources(resourceLocation.getHandle()).stream()
            .map(IResource::getLocation)
            .map(location -> get(location.getNamespace(), location.getPath()))
            .collect(Collectors.toSet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<ResourceLocation> getLoaded(String namespace) {
    return this.getLoaded(namespace, s -> true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<ResourceLocation> getLoaded(String namespace, Predicate<String> predicate) {
    return Minecraft.getInstance().getResourceManager()
            .getAllResourceLocations(namespace, predicate).stream()
            .map(location -> get(location.getNamespace(), location.getPath()))
            .collect(Collectors.toSet());
  }
}
