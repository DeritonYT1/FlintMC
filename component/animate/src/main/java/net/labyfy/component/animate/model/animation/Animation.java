package net.labyfy.component.animate.model.animation;

import net.labyfy.component.animate.Node;
import net.labyfy.component.animate.model.armature.Armature;
import net.labyfy.component.animate.scene.Scene;

public interface Animation {

  AnimationChannel[] getChannels();

  AnimationChannel getChannel(Node node);

  String getName();

  Scene getScene();

  double getTicksPerSecond();

  double getDuration();

}
