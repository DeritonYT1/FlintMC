package net.labyfy.component.render;

public interface RenderAdapter<T> {

  void render(T object, BufferBuilder bufferBuilder);

}
