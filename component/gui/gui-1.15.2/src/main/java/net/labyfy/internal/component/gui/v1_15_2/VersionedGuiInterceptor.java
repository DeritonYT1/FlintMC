package net.labyfy.internal.component.gui.v1_15_2;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import net.labyfy.component.gui.RenderExecution;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.mappings.MethodMapping;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.labyfy.component.transform.javassist.CtClassFilter;
import net.labyfy.component.transform.javassist.CtClassFilters;
import net.labyfy.internal.component.gui.DefaultGuiController;
import net.minecraft.client.Minecraft;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 1.15.2 Implementation of the gui interceptor
 */
@Singleton
@AutoLoad
public class VersionedGuiInterceptor {
  private final ClassMappingProvider mappingProvider;
  private final DefaultGuiController controller;

  @Inject
  private VersionedGuiInterceptor(ClassMappingProvider mappingProvider, DefaultGuiController controller) {
    this.mappingProvider = mappingProvider;
    this.controller = controller;
  }

  @ClassTransform
  @CtClassFilter(className = "net.minecraft.client.gui.screen.Screen", value = CtClassFilters.SUBCLASS_OF)
  private void hookScreenRender(ClassTransformContext context) throws CannotCompileException {
    MethodMapping renderMapping = mappingProvider
        .get("net.minecraft.client.gui.IRenderable")
        .getMethod("render", int.class, int.class, float.class);

    CtClass screenClass = context.getCtClass();
    for (CtMethod method : screenClass.getDeclaredMethods()) {
      if (!method.getName().equals(renderMapping.getName())) {
        continue;
      }

      /*
       * Adjustment of the render method:
       *
       * if(preRenderCallback(mouseX, mouseY, partialTicks) {
       *   postRenderCallback(true, mouseX, mouseY, partialTicks);
       *   return;
       * }
       * [... original method ...]
       * postRenderCallback(false, mouseX, mouseY, partialTicks);
       */

      method.insertBefore(
          "if(net.labyfy.internal.component.gui.v1_15_2.VersionedGuiInterceptor.preScreenRenderCallback($$)) {" +
              "   net.labyfy.internal.component.gui.v1_15_2.VersionedGuiInterceptor.postScreenRenderCallback(true, $$);" +
              "   return;" +
              "}"
      );

      method.insertAfter(
          "net.labyfy.internal.component.gui.v1_15_2.VersionedGuiInterceptor.postScreenRenderCallback(false, $$);");

      break;
    }
  }

  // Called from injected code, see above
  public static boolean preScreenRenderCallback(int mouseX, int mouseY, float partialTick) {
    DefaultGuiController controller = InjectionHolder.getInjectedInstance(VersionedGuiInterceptor.class).controller;

    RenderExecution execution = new RenderExecution(
        mouseX,
        mouseY,
        partialTick
    );

    controller.screenRenderCalled(
        Hook.ExecutionTime.BEFORE,
        execution
    );

    return execution.getCancellation().isCancelled();
  }

  // Called from injected code, see above
  public static void postScreenRenderCallback(boolean isCancelled, int mouseX, int mouseY, float partialTick) {
    DefaultGuiController controller = InjectionHolder.getInjectedInstance(VersionedGuiInterceptor.class).controller;

    RenderExecution execution = new RenderExecution(
        isCancelled,
        mouseX,
        mouseY,
        partialTick
    );

    controller.screenRenderCalled(
        Hook.ExecutionTime.AFTER,
        execution
    );
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "displayGuiScreen",
      parameters = @Type(typeName = "net.minecraft.client.gui.screen.Screen"),
      executionTime = Hook.ExecutionTime.AFTER,
      version = "1.15.2"
  )
  public void hookScreenChanged() {
    // Make sure to end input on screen change
    controller.safeEndInput();
    controller.screenChanged(Minecraft.getInstance().currentScreen);
  }

  @Hook(
      className = "net.minecraft.client.renderer.GameRenderer",
      methodName = "updateCameraAndRender",
      executionTime = Hook.ExecutionTime.AFTER,
      parameters = {
          @Type(reference = float.class),
          @Type(reference = long.class),
          @Type(reference = boolean.class)
      },
      version = "1.15.2"
  )
  public void hookAfterRender() {
    // Let the frame end here
    controller.endFrame();
  }
}
