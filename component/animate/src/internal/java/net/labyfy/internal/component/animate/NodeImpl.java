package net.labyfy.internal.component.animate;

import net.labyfy.component.animate.Node;
import net.labyfy.component.animate.model.mesh.Mesh;
import net.labyfy.internal.component.animate.model.armature.ArmatureImpl;
import net.labyfy.internal.component.animate.model.armature.BoneImpl;
import net.labyfy.internal.component.animate.model.mesh.MeshImpl;
import org.joml.Matrix4f;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AINode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class NodeImpl implements Node {

  private final AINode aiNode;
  private final String name;
  private final NodeImpl[] children;
  private final MeshImpl[] meshes;
  private final Matrix4f localTransform;
  private NodeImpl parent;

  public NodeImpl(NodeImpl node) {
    this.parent = node.parent;
    this.aiNode = node.aiNode;
    this.name = node.name;
    this.children = node.children;
    this.meshes = node.meshes;
    this.localTransform = node.localTransform;
  }

  public NodeImpl(NodeImpl parent, MeshImpl[] meshes, AINode aiNode) {
    this.parent = parent;
    this.aiNode = aiNode;
    this.name = aiNode.mName().dataString();
    this.children = new NodeImpl[aiNode.mNumChildren()];
    this.meshes = meshes;
    AIMatrix4x4 aiMatrix4x4 = aiNode.mTransformation();
    this.localTransform = new Matrix4f(
        aiMatrix4x4.a1(), aiMatrix4x4.a2(), aiMatrix4x4.a3(), aiMatrix4x4.a4(),
        aiMatrix4x4.b1(), aiMatrix4x4.b2(), aiMatrix4x4.b3(), aiMatrix4x4.b4(),
        aiMatrix4x4.c1(), aiMatrix4x4.c2(), aiMatrix4x4.c3(), aiMatrix4x4.c4(),
        aiMatrix4x4.d1(), aiMatrix4x4.d2(), aiMatrix4x4.d3(), aiMatrix4x4.d4()
    );
    this.localTransform.transpose();
    this.loadGeneralChildren();
  }

  private void loadGeneralChildren() {
    for (int i = 0; i < this.children.length; i++) {
      this.children[i] = new NodeImpl(this, this.meshes, AINode.create(this.aiNode.mChildren().get(i)));
    }
  }

  public NodeImpl[] getChildren() {
    return children;
  }

  public NodeImpl[] getChildrenRecursive() {
    if (this.children.length == 0) return new NodeImpl[]{};
    return Arrays.stream(children)
        .map(node -> {
          Collection<NodeImpl> list = new ArrayList<>();
          list.add(node);
          list.addAll(Arrays.asList(node.getChildrenRecursive()));
          return list;
        })
        .flatMap(Collection::stream)
        .toArray(NodeImpl[]::new);
  }

  public String getName() {
    return name;
  }


  public NodeImpl getParent() {
    return parent;
  }

  public AINode getAiNode() {
    return aiNode;
  }

  protected NodeImpl setParent(NodeImpl parent) {
    this.parent = parent;
    return this;
  }

  public void replaceChild(NodeImpl node, NodeImpl newNode) {
    for (int i = 0; i < this.children.length; i++) {
      Node child = this.children[i];
      if (child.getName().equals(node.getName())) {
        this.children[i] = newNode;
        newNode.parent = this;
      }
    }
  }

  public Mesh[] getMeshes() {
    return meshes;
  }

  public void substantiate() {
    for (int childIndex = 0; childIndex < this.children.length; childIndex++) {
      NodeImpl child = this.children[childIndex];
      for (MeshImpl mesh : this.meshes) {
        for (int boneIndex = 0; boneIndex < mesh.getAiMesh().mNumBones(); boneIndex++) {
          AIBone aiBone = AIBone.create(mesh.getAiMesh().mBones().get(boneIndex));
          if (aiBone.mName().dataString().equals(child.getName())) {
            BoneImpl bone = new BoneImpl(aiBone, mesh, child);
            this.children[childIndex] = bone;
            bone.setParent(this);
          }
        }
      }
    }
    for (NodeImpl child : children) {
      child.substantiate();
    }
    if (!(this instanceof BoneImpl)) {
      for (Node child : this.children) {
        if (child instanceof BoneImpl) {
          ArmatureImpl armature = new ArmatureImpl(this);
          armature.getParent().replaceChild(this, armature);
          ((BoneImpl) child).getMesh().setArmature(armature);
          return;
        }
      }
    }
  }

  public Matrix4f getLocalTransform() {
    return localTransform;
  }

  public boolean hasParent() {
    return this.parent != null;
  }

}
