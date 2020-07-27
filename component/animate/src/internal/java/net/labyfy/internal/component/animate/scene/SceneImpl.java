package net.labyfy.internal.component.animate.scene;

import net.labyfy.component.animate.scene.Scene;
import net.labyfy.internal.component.animate.NodeImpl;
import net.labyfy.internal.component.animate.model.animation.AnimationImpl;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AIScene;

public class SceneImpl extends NodeImpl implements Scene {

  private final AnimationImpl[] animations;

  protected SceneImpl(AIScene aiScene, NodeImpl node) {
    super(node);
    this.animations = new AnimationImpl[aiScene.mNumAnimations()];

    for (int i = 0; i < this.animations.length; i++) {
      this.animations[i] = new AnimationImpl(this, AIAnimation.create(aiScene.mAnimations().get(i)));
    }
  }

  public AnimationImpl[] getAnimations() {
    return animations;
  }

  public NodeImpl getChildren(String name){
    for (NodeImpl node : this.getChildrenRecursive()) {
      if(node.getName().equals(name)){
        return node;
      }
    }
    return null;
  }


}
