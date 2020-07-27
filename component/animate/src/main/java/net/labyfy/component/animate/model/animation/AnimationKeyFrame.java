package net.labyfy.component.animate.model.animation;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface AnimationKeyFrame {

  boolean hasRotation();

  boolean hasScaling();

  boolean hasPosition();

  double getTime();

  Quaternionf getRotation();

  Vector3f getScaling();

  Vector3f getPosition();

}
