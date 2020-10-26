package net.labyfy.component.transform.minecraft.obfuscate;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.launcher.classloading.RootClassLoader;
import net.labyfy.component.launcher.classloading.common.ClassInformation;
import net.labyfy.component.launcher.classloading.common.CommonClassLoaderHelper;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.transform.asm.ASMUtils;
import net.labyfy.component.transform.exceptions.ClassTransformException;
import net.labyfy.component.transform.launchplugin.LateInjectedTransformer;
import net.labyfy.component.transform.minecraft.MinecraftTransformer;
import net.labyfy.component.transform.minecraft.obfuscate.remap.MinecraftClassRemapper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

/**
 * Deobfuscates all minecraft classes for which mappings are provided
 */
@Singleton
@MinecraftTransformer(priority = Integer.MIN_VALUE)
public class MinecraftInstructionObfuscator implements LateInjectedTransformer {

  private final MinecraftClassRemapper minecraftClassRemapper;
  private final RootClassLoader rootClassLoader;
  private final ClassMappingProvider classMappingProvider;
  private final boolean obfuscated;

  @Inject
  private MinecraftInstructionObfuscator(
      MinecraftClassRemapper minecraftClassRemapper, ClassMappingProvider classMappingProvider, @Named("obfuscated") boolean obfuscated) {
    this.minecraftClassRemapper = minecraftClassRemapper;
    this.classMappingProvider = classMappingProvider;
    this.obfuscated = obfuscated;
    assert this.getClass().getClassLoader() instanceof RootClassLoader;
    this.rootClassLoader = (RootClassLoader) getClass().getClassLoader();
  }

  @Override
  public byte[] transform(String className, byte[] classData) throws ClassTransformException {
    if (!obfuscated) return classData;
    if (!className.startsWith("net.labyfy")) return classData;

    ClassInformation classInformation;

    try {
      classInformation = CommonClassLoaderHelper.retrieveClass(this.rootClassLoader, className);
    } catch (IOException exception) {
      throw new ClassTransformException("Unable to retrieve class metadata: " + className, exception);
    }

    if (classInformation == null) return classData;

    ClassNode classNode = ASMUtils.getNode(classInformation.getClassBytes());
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    ClassVisitor classRemapper = new ClassRemapper(classWriter, minecraftClassRemapper);
    classNode.accept(classRemapper);
    return classWriter.toByteArray();
  }
}
