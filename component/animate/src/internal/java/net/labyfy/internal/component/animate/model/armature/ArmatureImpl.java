package net.labyfy.internal.component.animate.model.armature;

import net.labyfy.component.animate.model.armature.Armature;
import net.labyfy.internal.component.animate.NodeImpl;

import java.util.Arrays;

public class ArmatureImpl extends NodeImpl implements Armature {

  public ArmatureImpl(NodeImpl node) {
    super(node);
  }

  public BoneImpl getRootBone() {
    return Arrays
        .stream(this.getChildren())
        .filter(BoneImpl.class::isInstance)
        .map(BoneImpl.class::cast)
        .findFirst().get();
  }

  public BoneImpl[] getBonesRecursive() {
    return Arrays
        .stream(this.getChildrenRecursive())
        .filter(BoneImpl.class::isInstance)
        .map(BoneImpl.class::cast)
        .toArray(BoneImpl[]::new);
  }
}
