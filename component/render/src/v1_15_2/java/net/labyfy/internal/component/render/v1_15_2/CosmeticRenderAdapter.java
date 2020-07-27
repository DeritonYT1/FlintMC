package net.labyfy.internal.component.render.v1_15_2;


import net.labyfy.component.render.BufferBuilder;
import net.labyfy.component.render.RenderAdapter;

public class CosmeticRenderAdapter implements RenderAdapter<Object> {

  public void render(Object object, BufferBuilder bufferBuilder) {
    bufferBuilder
        .pos(0, 0, 0)
        .finishVertex();
  }

}
