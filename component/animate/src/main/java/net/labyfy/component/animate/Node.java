package net.labyfy.component.animate;

public interface Node {

  Node[] getChildren();

  Node[] getChildrenRecursive();

  Node getParent();

  boolean hasParent();

  String getName();

}
