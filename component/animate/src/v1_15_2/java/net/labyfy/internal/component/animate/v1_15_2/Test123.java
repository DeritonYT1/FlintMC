package net.labyfy.internal.component.animate.v1_15_2;

import com.mojang.blaze3d.systems.RenderSystem;
import javassist.CannotCompileException;
import javassist.CtMethod;
import net.labyfy.component.animate.scene.Scene;
import net.labyfy.component.animate.scene.SceneLoader;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.transform.asm.MethodVisit;
import net.labyfy.component.transform.asm.MethodVisitorContext;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.objectweb.asm.Opcodes;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.lang.reflect.Field;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

@Singleton
@AutoLoad
public class Test123 {

  private final SceneLoader sceneLoader;

  @Inject
  private Test123(SceneLoader sceneLoader) {
    this.sceneLoader = sceneLoader;
    Scene load = this.sceneLoader.load(new File("model.dae"));
  }

  @ClassTransform("net.minecraft.client.renderer.entity.PlayerRenderer")
  public void on(ClassTransformContext classTransformContext) throws CannotCompileException {
    for (CtMethod declaredMethod : classTransformContext.getCtClass().getMethods()) {
      if (declaredMethod.getName().equals("render")) {
        declaredMethod.insertAfter("net.labyfy.internal.component.animate.v1_15_2.Test123.blub();");
      }
    }
  }

  @MethodVisit(
      className = "net.minecraft.client.renderer.WorldRenderer",
      methodName = "updateCameraAndRender",
      desc = "(Lcom/mojang/blaze3d/matrix/MatrixStack;FJZLnet/minecraft/client/renderer/ActiveRenderInfo;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/renderer/Matrix4f;)V"
  )
  public void visit(MethodVisitorContext methodVisitorContext) {
    methodVisitorContext.storeAsset("finished", false);
    methodVisitorContext.onVisitMethodInsn((context) -> {
      context.write();
      if (context.getName().equals("finish")) {
        if (!methodVisitorContext.<Boolean>getAsset("finished")) {
          methodVisitorContext.storeAsset("finished", true);
          methodVisitorContext.svisitMethodInsn(Opcodes.INVOKESTATIC, "net/labyfy/internal/component/animate/v1_15_2/Test123", "test123", "()V");
        }
      }
    });
  }

  public static void blub() {
    System.out.println("Blub");
  }

  public static void test123() {
    try {
      Field renderTypeTextures = WorldRenderer.class.getDeclaredField("renderTypeTextures");
      renderTypeTextures.setAccessible(true);
      RenderTypeBuffers renderTypeBuffers = (RenderTypeBuffers) renderTypeTextures.get(Minecraft.getInstance().worldRenderer);
      renderTypeBuffers.getBufferSource().finish(Holder.cosmetic);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public static class Holder {
    static RenderType.State rendertype$state = RenderType.State.getBuilder()
        .transparency(new RenderState.TransparencyState("no_transparency", RenderSystem::disableBlend, () -> {
        }))
        .cull(new RenderState.CullState(false))
        .diffuseLighting(new RenderState.DiffuseLightingState(true))
        .lightmap(new RenderState.LightmapState(true))
        .overlay(new RenderState.OverlayState(false))
        .build(true);

    static RenderType cosmetic = RenderType.makeType("cosmetic", DefaultVertexFormats.POSITION, GL_TRIANGLES, 256, false, false, rendertype$state);
  }

}
