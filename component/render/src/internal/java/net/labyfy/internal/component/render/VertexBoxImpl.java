package net.labyfy.internal.component.render;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;

@Implement(VertexBox.class)
public class VertexBoxImpl implements VertexBox {

  //all sides of the box
  private final VertexQuad back;
  private final VertexQuad front;
  private final VertexQuad right;
  private final VertexQuad left;
  private final VertexQuad top;
  private final VertexQuad bottom;
  private Vector3f position;


  @AssistedInject
  private VertexBoxImpl(
      VertexQuad.Factory vertexQuadBuilderFactory,
      Vertex.Factory vertexFactory,
      @Assisted("position") Vector3f position,
      @Assisted("dimensions") Vector3f dimensions,
      @Assisted("textureOffset") Vector2f textureOffset,
      @Assisted("textureDensity") Vector2f textureDensity,
      @Assisted("color") Color color,
      @Assisted("lightMap") int lightMap
  ) {
    this.position = new Vector3f(position);
    float width = dimensions.x;
    float height = dimensions.y;
    float depth = dimensions.z;

    this.front = vertexQuadBuilderFactory.create(
        vertexFactory.create(0, 0, 0)
            .setTextureUV(textureOffset.x + depth / textureDensity.x, textureOffset.y + depth / textureDensity.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(-1, 0, 0),

        vertexFactory.create(width, 0, 0)
            .setTextureUV(textureOffset.x + (depth + width) / textureDensity.x, textureOffset.y + depth / textureDensity.y)

            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(-1, 0, 0),

        vertexFactory.create(width, height, 0)
            .setTextureUV(textureOffset.x + (depth + width) / textureDensity.x, textureOffset.y + (depth + height) / textureDensity.y)

            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(-1, 0, 0),

        vertexFactory.create(0, height, 0)
            .setTextureUV(textureOffset.x + depth / textureDensity.x, textureOffset.y + (depth + height) / textureDensity.y)

            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(-1, 0, 0)
    );

    this.back = vertexQuadBuilderFactory.create(
        vertexFactory.create(0, height, depth)
            .setTextureUV(textureOffset.x + ((depth + width + depth + width) / textureDensity.x), textureOffset.y + ((depth + height) / textureDensity.y))

            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(0, 0, -1)
        ,
        vertexFactory.create(0, 0, depth)
            .setTextureUV(textureOffset.x + ((depth + width + depth + width) / textureDensity.x), textureOffset.y + (depth / textureDensity.y))

            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(0, 0, -1)
        ,
        vertexFactory.create(width, 0, depth)
            .setTextureUV(textureOffset.x + ((depth + width + depth) / textureDensity.x), textureOffset.y + (depth / textureDensity.y))

            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(0, 0, -1)
        ,
        vertexFactory.create(width, height, depth)
            .setTextureUV(textureOffset.x + ((depth + width + depth) / textureDensity.x), textureOffset.y + ((depth + height) / textureDensity.y))

            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(0, 0, -1)
    );

    this.left = vertexQuadBuilderFactory.create(
        vertexFactory.create(0, 0, 0)
            .setTextureUV(textureOffset.x, textureOffset.y + (depth + height) / textureDensity.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(1, 0, 0),
        vertexFactory.create(0, 0, depth)
            .setTextureUV(textureOffset.x, textureOffset.y + depth / textureDensity.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(1, 0, 0),
        vertexFactory.create(0, height, depth)
            .setTextureUV(textureOffset.x + depth / textureDensity.x, textureOffset.y + depth / textureDensity.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(1, 0, 0),
        vertexFactory.create(0, height, 0)
            .setTextureUV(textureOffset.x + depth / textureDensity.x, textureOffset.y + (depth + height) / textureDensity.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(1, 0, 0)
    );

    this.right = vertexQuadBuilderFactory.create(
        vertexFactory.create(width, 0, 0)
            .setTextureUV(textureOffset.x + (depth + width) / textureDensity.x, textureOffset.y + depth / textureDensity.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(-1, 0, 0)
        ,
        vertexFactory.create(width, 0, depth)
            .setTextureUV(textureOffset.x + (depth + width + depth) / textureDensity.x, textureOffset.y + depth / textureDensity.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(-1, 0, 0)
        ,
        vertexFactory.create(width, height, depth)
            .setTextureUV(textureOffset.x + (depth + width + depth) / textureDensity.x, textureOffset.y + (depth + height) / textureDensity.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(-1, 0, 0)
        ,
        vertexFactory.create(width, height, 0)
            .setTextureUV(textureOffset.x + (depth + width) / textureDensity.x, textureOffset.y + (depth + height) / textureDensity.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(-1, 0, 0)
    );

    this.bottom = vertexQuadBuilderFactory.create(
        vertexFactory.create(0, height, 0)
            .setTextureUV(textureOffset.x + (width + depth) / textureDensity.x, textureOffset.y + depth / textureDensity.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(0, -1, 0),
        vertexFactory.create(0, height, depth)
            .setTextureUV(textureOffset.x + (width + depth) / textureDensity.x, textureOffset.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(0, -1, 0),
        vertexFactory.create(width, height, depth)
            .setTextureUV(textureOffset.x + (depth + width + width) / textureDensity.x, textureOffset.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(0, -1, 0),
        vertexFactory.create(width, height, 0)
            .setTextureUV(textureOffset.x + (depth + width + width) / textureDensity.x, textureOffset.y + depth / textureDensity.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(0, -1, 0)
    );

    this.top = vertexQuadBuilderFactory.create(
        vertexFactory.create(0, 0, 0)
            .setTextureUV(textureOffset.x + depth / textureDensity.x, textureOffset.y + depth / textureDensity.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(0, 1, 0),
        vertexFactory.create(0, 0, depth)
            .setTextureUV(textureOffset.x + depth / textureDensity.x, textureOffset.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(0, 1, 0),
        vertexFactory.create(width, 0, depth)
            .setTextureUV(textureOffset.x + (depth + width) / textureDensity.x, textureOffset.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(0, 1, 0),
        vertexFactory.create(width, 0, 0)
            .setTextureUV(textureOffset.x + (depth + width) / textureDensity.x, textureOffset.y + depth / textureDensity.y)
            .setColor(color)
            .setLightmapUV(lightMap)
            .setNormal(0, 1, 0)
    );
  }

  public VertexQuad getBack() {
    return this.back;
  }

  public VertexQuad getBottom() {
    return this.bottom;
  }

  public VertexQuad getFront() {
    return this.front;
  }

  public VertexQuad getLeft() {
    return this.left;
  }

  public VertexQuad getRight() {
    return this.right;
  }

  public VertexQuad getTop() {
    return this.top;
  }

  public Vector3f getPosition() {
    return new Vector3f(position);
  }

  public VertexBox setLightmapUV(int lightmapUV) {
    this.back.setLightmapUV(lightmapUV);
    this.front.setLightmapUV(lightmapUV);
    this.right.setLightmapUV(lightmapUV);
    this.left.setLightmapUV(lightmapUV);
    this.top.setLightmapUV(lightmapUV);
    this.bottom.setLightmapUV(lightmapUV);
    return this;
  }

  public VertexBox render(MatrixStack matrixStack, VertexBuffer vertexBuffer) {
    matrixStack.push()
        .translate(this.position);
    this.back.render(matrixStack, vertexBuffer);
    this.front.render(matrixStack, vertexBuffer);
    this.right.render(matrixStack, vertexBuffer);
    this.left.render(matrixStack, vertexBuffer);
    this.top.render(matrixStack, vertexBuffer);
    this.bottom.render(matrixStack, vertexBuffer);
    matrixStack.pop();
    return this;
  }
}
