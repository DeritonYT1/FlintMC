package net.labyfy.component.tasks.v1_16_3;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.tasks.TaskExecutionException;
import net.labyfy.component.tasks.TaskExecutor;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

@Singleton
@AutoLoad
public class VersionedTasks {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private VersionedTasks(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  @ClassTransform(value = "net.minecraft.client.Minecraft", version = "1.16.3")
  public void transformPostMinecraft(ClassTransformContext context) throws CannotCompileException {
    context.getCtClass()
            .getDeclaredConstructors()[0]
            .insertAfter("net.labyfy.component.tasks.v1_16_3.VersionedTasks.notify(\""
            + Tasks.POST_MINECRAFT_INITIALIZE
            + "\");");
  }

  @ClassTransform(value = "net.minecraft.client.MainWindow", version = "1.16.3")
  public void transformOpenGlInitialize(ClassTransformContext context) throws CannotCompileException, NotFoundException {
    context.getCtClass()
            .getDeclaredMethod(
                    this.classMappingProvider
                    .get("net.minecraft.client.MainWindow")
                    .getMethod("setLogOnGlError")
                    .getName()
            ).insertAfter(
            "net.labyfy.component.tasks.v1_16_3.VersionedTasks.notify(\""
                    + Tasks.POST_OPEN_GL_INITIALIZE
                    + "\");"
    );
  }

  public static void notify(Tasks task) throws TaskExecutionException {
    InjectionHolder.getInjectedInstance(TaskExecutor.class).execute(task);
  }
}
