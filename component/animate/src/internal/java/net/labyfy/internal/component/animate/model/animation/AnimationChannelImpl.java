package net.labyfy.internal.component.animate.model.animation;

import net.labyfy.component.animate.model.animation.Animation;
import net.labyfy.component.animate.model.animation.AnimationChannel;
import net.labyfy.internal.component.animate.NodeImpl;
import net.labyfy.internal.component.animate.scene.SceneImpl;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.assimp.AINodeAnim;

import java.util.*;
import java.util.stream.Collectors;

public class AnimationChannelImpl implements AnimationChannel {
  private final AnimationImpl animation;
  private final AINodeAnim aiNodeAnim;
  private final NodeImpl target;
  private final AnimationKeyFrameImpl[] animationKeyFrames;

  public AnimationChannelImpl(AnimationImpl animation,  AINodeAnim aiNodeAnim) {
    this.animation = animation;
    this.aiNodeAnim = aiNodeAnim;
    this.target = animation.getScene().getChildren(aiNodeAnim.mNodeName().dataString());

    Map<Double, Vector3f> scalings = new HashMap<>();
    Map<Double, Vector3f> positions = new HashMap<>();
    Map<Double, Quaternionf> rotations = new HashMap<>();

    Set<Double> times = new HashSet<>();
    times.addAll(scalings.keySet());
    times.addAll(positions.keySet());
    times.addAll(rotations.keySet());

    this.animationKeyFrames = new AnimationKeyFrameImpl[times.size()];
    List<Double> sortedTimes = times.stream()
        .sorted(Double::compareTo)
        .collect(Collectors.toList());

    for (int i = 0; i < sortedTimes.size(); i++) {
      Double time = sortedTimes.get(i);
      this.animationKeyFrames[i] = new AnimationKeyFrameImpl(
          time,
          rotations.get(time),
          scalings.get(time),
          positions.get(time)
      );
    }
  }

  public AINodeAnim getAiNodeAnim() {
    return aiNodeAnim;
  }

  public Animation getAnimation() {
    return this.animation;
  }

  public NodeImpl getTarget() {
    return target;
  }


}
