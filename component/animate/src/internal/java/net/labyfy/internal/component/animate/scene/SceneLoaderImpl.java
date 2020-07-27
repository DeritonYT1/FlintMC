package net.labyfy.internal.component.animate.scene;

import net.labyfy.component.animate.model.mesh.Mesh;
import net.labyfy.component.animate.scene.Scene;
import net.labyfy.component.animate.scene.SceneLoader;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.internal.component.animate.NodeImpl;
import net.labyfy.internal.component.animate.model.mesh.MeshImpl;
import org.joml.Matrix4f;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

import javax.inject.Singleton;
import java.io.File;

@Singleton
@Implement(SceneLoader.class)
public class SceneLoaderImpl implements SceneLoader {
  public SceneImpl load(File file) {
    return this.load(Assimp.aiImportFile(file.getPath(), Assimp.aiProcess_Triangulate));
  }

  public SceneImpl load(AIScene aiScene) {
    MeshImpl[] meshes = new MeshImpl[aiScene.mNumMeshes()];
    for (int i = 0; i < meshes.length; i++) {
      meshes[i] = new MeshImpl(i, AIMesh.create(aiScene.mMeshes().get(i)));
    }
    NodeImpl root = new NodeImpl(null, meshes, aiScene.mRootNode());
    root.substantiate();
    for (MeshImpl mesh : meshes) {
      if (mesh.getArmature() != null) {
        mesh.getArmature().getRootBone().calculateInverseBindTransform(new Matrix4f());
      }
    }
    return new SceneImpl(aiScene, root);
  }

}
