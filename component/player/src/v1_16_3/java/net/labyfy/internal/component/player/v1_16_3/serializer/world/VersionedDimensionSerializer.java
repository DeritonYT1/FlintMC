package net.labyfy.internal.component.player.v1_16_3.serializer.world;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.serializer.world.DimensionSerializer;
import net.labyfy.component.player.world.Dimension;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;

/**
 * 1.16.3 implementation of {@link DimensionSerializer}
 */
@Singleton
@Implement(value = DimensionSerializer.class, version = "1.16.3")
public class VersionedDimensionSerializer implements DimensionSerializer<ResourceLocation> {

  @Override
  public Dimension deserialize(ResourceLocation value) {
    switch (value.getPath()) {
      case "overworld":
        return Dimension.OVERWORLD;
      case "the_nether":
        return Dimension.NETHER;
      case "the_end":
        return Dimension.THE_END;
      default:
        throw new IllegalStateException("Unexpected value: " + value.getPath());
    }
  }

  @Override
  public ResourceLocation serialize(Dimension value) {
    switch (value) {
      case OVERWORLD:
        return DimensionType.field_242710_a;
      case NETHER:
        return DimensionType.field_242711_b;
      case THE_END:
        return DimensionType.field_242712_c;
      default:
        throw new IllegalStateException("Unexpected value: " + value);
    }
  }
}
