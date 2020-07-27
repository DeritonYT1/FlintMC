package net.labyfy.internal.component.render;

import net.labyfy.component.render.VertexFormatElement;

public class VertexFormatElementImpl implements VertexFormatElement {

  private final String name;
  private final int bytes;

  public VertexFormatElementImpl(String name, int bytes) {
    this.name = name;
    this.bytes = bytes;
  }

  public String getName() {
    return this.name;
  }

  public int getBytes() {
    return this.bytes;
  }
}
