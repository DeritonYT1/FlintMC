package net.labyfy.internal.component.animate.model.animation;

import net.labyfy.component.animate.model.animation.AnimationKeyFrame;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AnimationKeyFrameImpl implements AnimationKeyFrame {

  private final double time;
  private final Quaternionf rotation;
  private final Vector3f scaling;
  private final Vector3f position;

  public AnimationKeyFrameImpl(double time, Quaternionf rotation, Vector3f scaling, Vector3f position) {
    this.time = time;
    this.rotation = rotation;
    this.scaling = scaling;
    this.position = position;
  }


  public boolean hasRotation() {
    return this.rotation != null;
  }

  public boolean hasScaling() {
    return this.scaling != null;
  }

  public boolean hasPosition() {
    return this.position != null;
  }

  public double getTime() {
    return this.time;
  }

  public Quaternionf getRotation() {
    return new Quaternionf(this.rotation);
  }

  public Vector3f getScaling() {
    return new Vector3f(this.scaling);
  }

  public Vector3f getPosition() {
    return new Vector3f(this.position);
  }
}
