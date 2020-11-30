package net.flintmc.render.minecraft.type;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.util.Collection;

public interface RenderType {

  RenderType setupState();

  RenderType destroyState();

  Collection<RenderState> getRenderStates();

  RenderType addRenderState(RenderState renderState);

  @AssistedFactory(RenderType.class)
  interface Factory {
    RenderType create(@Assisted String name);
  }
}
