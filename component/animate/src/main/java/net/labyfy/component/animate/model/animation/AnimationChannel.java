package net.labyfy.component.animate.model.animation;

import net.labyfy.component.animate.model.armature.bone.Bone;

public interface AnimationChannel {

  Animation getAnimation();

  Bone getTarget();

}
