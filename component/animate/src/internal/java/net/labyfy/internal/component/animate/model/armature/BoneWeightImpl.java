package net.labyfy.internal.component.animate.model.armature;

import net.labyfy.component.animate.model.armature.bone.BoneWeight;
import net.labyfy.internal.component.animate.model.mesh.VertexImpl;

public class BoneWeightImpl implements BoneWeight {

  private final BoneImpl bone;
  private final VertexImpl vertex;
  private final float weight;

  public BoneWeightImpl(BoneImpl bone, VertexImpl vertex, float weight) {
    this.bone = bone;
    this.vertex = vertex;
    this.weight = weight;
  }

  public float getWeight() {
    return weight;
  }

  public VertexImpl getVertex() {
    return vertex;
  }

  public BoneImpl getBone() {
    return bone;
  }
}
