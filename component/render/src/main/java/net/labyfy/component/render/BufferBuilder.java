package net.labyfy.component.render;

public interface BufferBuilder {
  BufferBuilder pos(float x, float y, float z);

  BufferBuilder normal(float x, float y, float z);

  BufferBuilder finishVertex();

  BufferBuilder growBufferEventually(int targetSize);

  VertexFormat getFormat();

  int getVertexCount();
  
}
