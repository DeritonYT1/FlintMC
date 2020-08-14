package net.labyfy.internal.component.render;

import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.MatrixStack;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexTriangle;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class VertexTriangleImpl implements VertexTriangle {

  private final Supplier<Vector3f>
      vertex1,
      vertex2,
      vertex3;

  private final Supplier<Color> color;

  private final Supplier<Vector2f>
      vertex1TextureUV,
      vertex2TextureUV,
      vertex3TextureUV;

  private final IntSupplier lightMap;

  private VertexTriangleImpl(Supplier<Vector3f> vertex1, Supplier<Vector3f> vertex2, Supplier<Vector3f> vertex3, Supplier<Color> color, Supplier<Vector2f> vertex1TextureUV, Supplier<Vector2f> vertex2TextureUV, Supplier<Vector2f> vertex3TextureUV, IntSupplier lightMap) {
    this.vertex1 = vertex1;
    this.vertex2 = vertex2;
    this.vertex3 = vertex3;
    this.color = color;
    this.vertex1TextureUV = vertex1TextureUV;
    this.vertex2TextureUV = vertex2TextureUV;
    this.vertex3TextureUV = vertex3TextureUV;
    this.lightMap = lightMap;
  }

  public Vector3f getVertex1() {
    return this.vertex1.get();
  }

  public Vector3f getVertex2() {
    return this.vertex2.get();
  }

  public Vector3f getVertex3() {
    return this.vertex3.get();
  }

  public Vector3f[] getVertices() {
    return new Vector3f[]{
        this.getVertex1(), this.getVertex2(), this.getVertex3()
    };
  }

  public Vector2f getVertex1TextureUV() {
    return vertex1TextureUV.get();
  }

  public Vector2f getVertex2TextureUV() {
    return vertex2TextureUV.get();
  }

  public Vector2f getVertex3TextureUV() {
    return vertex3TextureUV.get();
  }

  public Vector2f[] getVertexTextureUVs() {
    return new Vector2f[]{this.getVertex1TextureUV(), this.getVertex2TextureUV(), this.getVertex3TextureUV()};
  }

  public int getLightMap() {
    return this.lightMap.getAsInt();
  }

  public Color getColor() {
    return this.color.get();
  }

  public VertexTriangle render(MatrixStack matrixStack, VertexBuffer vertexBuffer) {
    matrixStack
        .push()
        .scale(1, -1, -1);
    vertexBuffer
        .pos(this.getVertex1())
        .color(this.getColor())
        .lightmap(this.getLightMap())
        .texture(this.getVertex1TextureUV())
        .end()
        .pos(this.getVertex2())
        .color(this.getColor())
        .lightmap(this.getLightMap())
        .texture(this.getVertex2TextureUV())
        .end()
        .pos(this.getVertex3())
        .color(this.getColor())
        .lightmap(this.getLightMap())
        .texture(this.getVertex3TextureUV())
        .end();
    matrixStack.pop();
    return this;
  }

  @Implement(Builder.class)
  public static class BuilderImpl implements Builder {

    private Supplier<Vector3f>
        vertex1,
        vertex2,
        vertex3;

    private Supplier<Color> color = () -> null;

    private IntSupplier lightMap = () -> 0;

    private Supplier<Vector2f>
        vertex1TextureUV = () -> null,
        vertex2TextureUV = () -> null,
        vertex3TextureUV = () -> null;

    @AssistedInject
    private BuilderImpl() {
    }


    public Builder withVertices(Supplier<Vector3f> vertex1, Supplier<Vector3f> vertex2, Supplier<Vector3f> vertex3) {
      this.vertex1 = vertex1;
      this.vertex2 = vertex2;
      this.vertex3 = vertex3;
      return this;
    }

    public Builder withTextureUVs(Supplier<Vector2f> vertex1TextureUV, Supplier<Vector2f> vertex2TextureUV, Supplier<Vector2f> vertex3TextureUV) {
      this.vertex1TextureUV = vertex1TextureUV;
      this.vertex2TextureUV = vertex2TextureUV;
      this.vertex3TextureUV = vertex3TextureUV;
      return this;
    }


    public Builder withTextureUVs(Vector2f vertex1TextureUV, Vector2f vertex2TextureUV, Vector2f vertex3TextureUV) {
      return this.withTextureUVs(() -> vertex1TextureUV, () -> vertex2TextureUV, () -> vertex3TextureUV);
    }

    public Builder withVertices(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3) {
      return this.withVertices(() -> vertex1, () -> vertex2, () -> vertex3);
    }

    public Builder withColor(Supplier<Color> color) {
      this.color = color;
      return this;
    }

    public Builder withColor(Color color) {
      return this.withColor(() -> color);
    }

    public Builder withLightMap(int lightMap) {
      return this.withLightMap(() -> lightMap);
    }

    public Builder withLightMap(IntSupplier lightMap) {
      this.lightMap = lightMap;
      return this;
    }

    public VertexTriangle build() {
      Objects.requireNonNull(this.vertex1);
      Objects.requireNonNull(this.vertex2);
      Objects.requireNonNull(this.vertex3);
      return new VertexTriangleImpl(this.vertex1, this.vertex2, this.vertex3, this.color, this.vertex1TextureUV, this.vertex2TextureUV, this.vertex3TextureUV, this.lightMap);
    }
  }

}