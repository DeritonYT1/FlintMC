package net.labyfy.internal.component.render;

import net.labyfy.component.render.BufferBuilder;
import net.labyfy.component.render.VertexFormat;

import java.nio.ByteBuffer;

public class BufferBuilderImpl implements BufferBuilder {

  private final VertexFormat vertexFormat;
  private ByteBuffer byteBuffer;
  private int vertexCount;
  private int writtenBytes;

  public BufferBuilderImpl(VertexFormat vertexFormat) {
    this.vertexFormat = vertexFormat;
    this.byteBuffer = ByteBuffer.allocateDirect(this.vertexFormat.getBytes());
  }

  public BufferBuilderImpl pos(float x, float y, float z) {
    return this.pushFloats("position", x, y, z);
  }

  public BufferBuilderImpl normal(float x, float y, float z) {
    return this.pushFloats("normal", x, y, z);
  }

  public BufferBuilderImpl pushFloats(String name, float... floats) {
    this.growBufferEventually(((this.vertexCount + 1) * this.vertexFormat.getBytes()));
    vertexFormat.pushFloats(this.byteBuffer, this, name, floats);
    this.writtenBytes += floats.length * Float.BYTES;
    return this;
  }

  public BufferBuilderImpl pushBytes(String name, byte... bytes) {
    this.growBufferEventually(((this.vertexCount + 1) * this.vertexFormat.getBytes()));
    vertexFormat.pushBytes(this.byteBuffer, this, name, bytes);
    this.writtenBytes += bytes.length;
    return this;
  }

  public BufferBuilderImpl finishVertex() {
    this.vertexCount++;
    if (this.writtenBytes != this.vertexCount * this.vertexFormat.getBytes()) {
      throw new IllegalStateException("Not all or too many vertex elements have been written.");
    }
    return this;
  }

  public BufferBuilderImpl growBufferEventually(int targetSize) {
    if (this.byteBuffer.limit() < targetSize) {
      ByteBuffer oldBuffer = this.byteBuffer;
      this.byteBuffer = ByteBuffer.allocateDirect(targetSize);
      this.byteBuffer.put(oldBuffer);
    }
    return this;
  }

  public VertexFormat getFormat() {
    return this.vertexFormat;
  }

  public int getVertexCount() {
    return this.vertexCount;
  }


}
