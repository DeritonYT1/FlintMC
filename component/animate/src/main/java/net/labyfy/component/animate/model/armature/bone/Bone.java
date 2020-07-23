package net.labyfy.component.animate.model.armature.bone;

import net.labyfy.component.animate.Node;

public interface Bone extends Node {

  Bone[] getChildren();

  Bone[] getChildrenRecursive();

  BoneWeight[] getWeights();

}
