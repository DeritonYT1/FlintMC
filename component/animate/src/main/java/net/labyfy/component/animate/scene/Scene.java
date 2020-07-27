package net.labyfy.component.animate.scene;

import net.labyfy.component.animate.Node;
import net.labyfy.component.animate.model.mesh.Mesh;

public interface Scene extends Node {

  Mesh[] getMeshes();

}
