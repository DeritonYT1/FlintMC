package net.labyfy.internal.component.render;

import net.labyfy.component.render.BufferBuilder;
import net.labyfy.component.render.VertexFormat;
import net.labyfy.component.render.VertexFormatElement;

import java.nio.ByteBuffer;

public class VertexFormatImpl implements VertexFormat {

  private final VertexFormatElementImpl[] elements;

  public VertexFormatImpl(VertexFormatElementImpl[] elements) {
    this.elements = elements;
  }

  public VertexFormatImpl pushFloats(ByteBuffer byteBuffer, BufferBuilder bufferBuilder, String name, float... floats) {
    int offset = (bufferBuilder.getVertexCount() * bufferBuilder.getFormat().getBytes()) + bufferBuilder.getFormat().getByteOffset(name);
    byteBuffer.position(offset);
    for (float f : floats) {
      byteBuffer.putFloat(f);
    }
    return this;
  }

  public VertexFormat pushBytes(ByteBuffer byteBuffer, BufferBuilder bufferBuilder, String name, byte... bytes) {
    int offset = (bufferBuilder.getVertexCount() * bufferBuilder.getFormat().getBytes()) + bufferBuilder.getFormat().getByteOffset(name);
    byteBuffer.position(offset);
    byteBuffer.put(bytes);
    return this;
  }

  public VertexFormat pushBytes(ByteBuffer byteBuffer, BufferBuilder bufferBuilder, String name, ByteBuffer bytes) {
    int offset = (bufferBuilder.getVertexCount() * bufferBuilder.getFormat().getBytes()) + bufferBuilder.getFormat().getByteOffset(name);
    byteBuffer.position(offset);
    byteBuffer.put(bytes);
    return this;
  }

  public VertexFormatElementImpl[] getElements() {
    return this.elements;
  }

  public boolean hasElement(String name) {
    for (VertexFormatElementImpl element : this.elements) {
      if (element.getName().equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }

  public VertexFormatElement getElement(String name) {
    for (VertexFormatElementImpl element : this.elements) {
      if (element.getName().equalsIgnoreCase(name)) {
        return element;
      }
    }
    return null;
  }

  public int getBytes() {
    int bytes = 0;
    for (VertexFormatElementImpl element : this.elements) {
      bytes += element.getBytes();
    }
    return bytes;
  }

  public int getByteOffset(String name) {
    int offset = 0;
    for (VertexFormatElementImpl element : this.elements) {
      if(name.equalsIgnoreCase(element.getName())){
        return offset;
      }
      offset+=element.getBytes();
    }
    throw new IllegalStateException();
  }
}
