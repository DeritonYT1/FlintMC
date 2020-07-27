package net.labyfy.internal.component.animate.model.mesh;

import net.labyfy.component.animate.model.armature.Armature;
import net.labyfy.component.animate.model.mesh.Face;
import net.labyfy.component.animate.model.mesh.Mesh;
import net.labyfy.component.animate.model.mesh.Vertex;
import net.labyfy.internal.component.animate.model.armature.ArmatureImpl;
import org.lwjgl.assimp.AIMesh;

public class MeshImpl implements Mesh {

  private final int index;
  private final AIMesh aiMesh;
  private final String name;
  private final VertexImpl[] vertices;
  private final FaceImpl[] faces;
  private ArmatureImpl armature;

  public MeshImpl(int index, AIMesh aiMesh) {
    this.index = index;
    this.aiMesh = aiMesh;
    this.name = aiMesh.mName().dataString();
    this.vertices = new VertexImpl[aiMesh.mNumVertices()];
    for (int i = 0; i < this.vertices.length; i++) {
      this.vertices[i] = new VertexImpl(i, this, aiMesh.mVertices().get(i));
    }
    this.faces = new FaceImpl[aiMesh.mNumFaces()];
    for (int i = 0; i < this.faces.length; i++) {
      this.faces[i] = new FaceImpl(this, aiMesh.mFaces().get(i));
    }
  }

  public AIMesh getAiMesh() {
    return aiMesh;
  }


  public String getName() {
    return name;
  }

  public MeshImpl setArmature(ArmatureImpl armature) {
    this.armature = armature;
    return this;
  }

  public int getIndex() {
    return index;
  }

  public FaceImpl[] getFaces() {
    return faces;
  }

  public ArmatureImpl getArmature() {
    return armature;
  }

  public boolean hasArmature() {
    return this.armature != null;
  }

  public VertexImpl[] getVertices() {
    return this.vertices;
  }

}
