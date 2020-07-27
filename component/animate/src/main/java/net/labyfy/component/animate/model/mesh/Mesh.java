package net.labyfy.component.animate.model.mesh;

import net.labyfy.component.animate.model.armature.Armature;

public interface Mesh {

  Armature getArmature();

  boolean hasArmature();

  Face[] getFaces();

  Vertex[] getVertices();

  int getIndex();

}
