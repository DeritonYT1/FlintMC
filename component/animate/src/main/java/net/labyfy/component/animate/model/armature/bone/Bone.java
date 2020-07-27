package net.labyfy.component.animate.model.armature.bone;

import net.labyfy.component.animate.Node;
import net.labyfy.component.animate.model.mesh.Mesh;

public interface Bone extends Node {

  Bone[] getChildren();

  Bone[] getChildrenRecursive();

  BoneWeight[] getWeights();

  boolean isRoot();

  Mesh getMesh();

}
