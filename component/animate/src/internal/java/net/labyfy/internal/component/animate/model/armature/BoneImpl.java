package net.labyfy.internal.component.animate.model.armature;

import net.labyfy.component.animate.Node;
import net.labyfy.component.animate.model.armature.bone.Bone;
import net.labyfy.component.animate.model.armature.bone.BoneWeight;
import net.labyfy.component.animate.model.mesh.Vertex;
import net.labyfy.internal.component.animate.NodeImpl;
import net.labyfy.internal.component.animate.model.mesh.MeshImpl;
import org.joml.Matrix4f;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIVertexWeight;

import java.util.Collection;
import java.util.LinkedHashSet;

public class BoneImpl extends NodeImpl implements Bone {

  private final AIBone aiBone;
  private final MeshImpl mesh;
  private final BoneWeightImpl[] boneWeights;
  private Matrix4f inverseBindTransform = new Matrix4f();


  public BoneImpl(AIBone aiBone, MeshImpl mesh, NodeImpl node) {
    super(node);
    this.aiBone = aiBone;
    this.mesh = mesh;
    this.boneWeights = new BoneWeightImpl[aiBone.mNumWeights()];
    for (int i = 0; i < this.boneWeights.length; i++) {
      AIVertexWeight aiVertexWeight = aiBone.mWeights().get(i);
      this.boneWeights[i] = new BoneWeightImpl(this, this.mesh.getVertices()[aiVertexWeight.mVertexId()], aiVertexWeight.mWeight());
    }
  }

  public MeshImpl getMesh() {
    return mesh;
  }

  public BoneWeight[] getBoneWeights() {
    return boneWeights;
  }

  public BoneImpl[] getChildren() {
    Collection<BoneImpl> bones = new LinkedHashSet<>();
    for (Node superChild : super.getChildren()) {
      if (superChild instanceof BoneImpl) {
        bones.add(((BoneImpl) superChild));
      }
    }
    return bones.toArray(new BoneImpl[]{});
  }

  public BoneImpl[] getChildrenRecursive() {
    Collection<BoneImpl> bones = new LinkedHashSet<>();
    for (Node superChild : super.getChildrenRecursive()) {
      if (superChild instanceof BoneImpl) {
        bones.add(((BoneImpl) superChild));
      }
    }
    return bones.toArray(new BoneImpl[]{});
  }

  public float getWeight(Vertex vertex) {
    BoneWeight result = null;
    for (BoneWeight boneWeight : this.boneWeights) {
      if (boneWeight.getVertex().getIndex() == vertex.getIndex()
          && vertex.getMesh().getIndex() == this.getMesh().getIndex()
          && boneWeight.getWeight() > 0) {
        if (result != null) {
          throw new IllegalStateException();
        }
        result = boneWeight;
      }
    }

    return result == null ? 0 : result.getWeight();
  }

  public void calculateInverseBindTransform(Matrix4f parentBindTransform) {
    Matrix4f bindTransform = parentBindTransform.mul(this.getLocalTransform(), new Matrix4f());
    bindTransform.invert(this.inverseBindTransform);
    for (BoneImpl childBone : this.getChildren()) {
      childBone.calculateInverseBindTransform(bindTransform);
    }
  }

  public Matrix4f getInverseBindTransform() {
    return inverseBindTransform;
  }

  public BoneWeightImpl[] getWeights() {
    return this.boneWeights;
  }

  public boolean isRoot() {
    return !(this.getParent() instanceof BoneImpl);
  }
}
