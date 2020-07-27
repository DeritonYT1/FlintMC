package net.labyfy.internal.component.animate.model.animation;

import net.labyfy.component.animate.Node;
import net.labyfy.component.animate.model.animation.Animation;
import net.labyfy.component.animate.model.animation.AnimationChannel;
import net.labyfy.component.animate.model.armature.Armature;
import net.labyfy.internal.component.animate.model.armature.ArmatureImpl;
import net.labyfy.internal.component.animate.scene.SceneImpl;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AINodeAnim;

public class AnimationImpl implements Animation {

  private final AIAnimation aiAnimation;
  private final SceneImpl scene;
  private final String name;
  private final double duration;
  private final double ticksPerSecond;
  private final AnimationChannelImpl[] animationChannels;

  public AnimationImpl(SceneImpl scene, AIAnimation aiAnimation) {
    this.aiAnimation = aiAnimation;
    this.scene = scene;
    this.name = aiAnimation.mName().dataString();
    this.duration = aiAnimation.mDuration();
    this.ticksPerSecond = aiAnimation.mTicksPerSecond();
    this.animationChannels = new AnimationChannelImpl[aiAnimation.mNumChannels()];

    for (int i = 0; i < this.animationChannels.length; i++) {
      this.animationChannels[i] = new AnimationChannelImpl(this, AINodeAnim.create(aiAnimation.mChannels().get(i)));
    }
  }

  public String getName() {
    return name;
  }

  public SceneImpl getScene() {
    return scene;
  }

  public double getTicksPerSecond() {
    return ticksPerSecond;
  }

  public double getDuration() {
    return duration;
  }

  public AnimationChannelImpl[] getAnimationChannels() {
    return animationChannels;
  }

  public AIAnimation getAiAnimation() {
    return aiAnimation;
  }

  public AnimationChannelImpl getChannel(Node node){
    for (AnimationChannelImpl animationChannel : this.animationChannels) {
      if(animationChannel.getTarget().getName().equals(node.getName())){
        return animationChannel;
      }
    }
    return null;
  }

  public AnimationChannelImpl[] getChannels() {
    return this.animationChannels;
  }

}
