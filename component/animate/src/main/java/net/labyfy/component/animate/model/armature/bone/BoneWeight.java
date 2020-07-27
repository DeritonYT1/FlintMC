package net.labyfy.component.animate.model.armature.bone;

import net.labyfy.component.animate.model.mesh.Vertex;

public interface BoneWeight {

  Vertex getVertex();

  Bone getBone();

  float getWeight();

}
