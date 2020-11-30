package net.flintmc.render.minecraft.v1_15_2.type;

import net.flintmc.render.minecraft.type.RenderState;
import net.flintmc.render.minecraft.type.RenderType;

import java.util.Collection;
import java.util.HashSet;

public class DefaultRenderType implements RenderType {

  private final Collection<RenderState> renderStates = new HashSet<>();

  public RenderType setupState() {
    for (RenderState renderState : this.renderStates) {
      renderState.setupState();
    }
    return this;
  }

  public RenderType destroyState() {
    for (RenderState renderState : this.renderStates) {
      renderState.destroyState();
    }
    return this;
  }

  public Collection<RenderState> getRenderStates() {
    return renderStates;
  }

  public RenderType addRenderState(RenderState renderState) {
    this.renderStates.add(renderState);
    return this;
  }
}
