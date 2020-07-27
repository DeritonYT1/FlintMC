package net.labyfy.internal.component.render;

public class Test123 {

  public static void main(String[] args) {
    new BufferBuilderImpl(
        new VertexFormatImpl(
            new VertexFormatElementImpl[]{
                new VertexFormatElementImpl("position", Float.BYTES * 3),
                new VertexFormatElementImpl("normal", Float.BYTES * 3)
            }));
  }

}
