package net.labyfy.internal.component.animate.model.mesh;

import net.labyfy.component.animate.model.mesh.Face;
import org.lwjgl.assimp.AIFace;

public class FaceImpl implements Face {

  private final MeshImpl mesh;
  private final AIFace aiFace;
  private final VertexImpl[] vertices;

  public FaceImpl(MeshImpl mesh, AIFace aiFace){
    this.mesh = mesh;
    this.aiFace = aiFace;
    this.vertices = new VertexImpl[aiFace.mNumIndices()];
    for (int i = 0; i < this.vertices.length; i++) {
      this.vertices[i] = mesh.getVertices()[aiFace.mIndices().get(i)];
    }
  }

  public MeshImpl getMesh() {
    return mesh;
  }

  public AIFace getAiFace() {
    return aiFace;
  }

  public VertexImpl[] getVertices() {
    return vertices;
  }

}
