package net.labyfy.internal.component.animate.model.mesh;

import net.labyfy.component.animate.model.mesh.Mesh;
import net.labyfy.component.animate.model.mesh.Vertex;
import org.lwjgl.assimp.AIVector3D;

public class VertexImpl implements Vertex {

  private final int index;
  private final MeshImpl mesh;
  private float x;
  private float y;
  private float z;

  public VertexImpl(int index, MeshImpl mesh, float x, float y, float z) {
    this.index = index;
    this.mesh = mesh;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public VertexImpl(int index, MeshImpl mesh, AIVector3D aiVector3D) {
    this.index = index;
    this.mesh = mesh;
    this.x = aiVector3D.x();
    this.y = aiVector3D.y();
    this.z = aiVector3D.z();
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public float getZ() {
    return z;
  }

  public int getIndex() {
    return index;
  }

  public MeshImpl getMesh() {
    return mesh;
  }
}
