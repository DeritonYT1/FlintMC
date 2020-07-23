package net.labyfy.component.animate.model.armature;

import net.labyfy.component.animate.Node;
import net.labyfy.component.animate.model.armature.bone.Bone;

public interface Armature extends Node {

  Bone getRootBone();

}
