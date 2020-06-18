package net.labyfy.component.transform.launchplugin;

import com.google.common.collect.*;
import io.sentry.Sentry;
import io.sentry.event.BreadcrumbBuilder;
import javassist.*;
import net.labyfy.component.initializer.Initializer;
import net.labyfy.component.initializer.EntryPoint;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.launcher.classloading.RootClassLoader;
import net.labyfy.component.launcher.service.LauncherPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class LabyfyLauncherPlugin implements LauncherPlugin {
  private static LabyfyLauncherPlugin instance;

  public static LabyfyLauncherPlugin getInstance() {
    if (instance == null) {
      throw new IllegalStateException("LabyfyLauncherPlugin has not been instantiated yet");
    }
    return instance;
  }

  private final Logger logger;
  private final Multimap<Integer, LateInjectedTransformer> injectedTransformers;

  private List<String> launchArguments;

  public LabyfyLauncherPlugin() {
    if (instance != null) {
      throw new IllegalStateException("LabyfyLauncherPlugin instantiated already");
    }
    RootClassLoader rootLoader = LaunchController.getInstance().getRootLoader();

    ClassPool.getDefault().appendClassPath(new ClassPath() {
      public InputStream openClassfile(String classname) throws NotFoundException {
        URL result = find(classname);
        if (result == null) {
          throw new NotFoundException("Class " + classname + " not found");
        }

        try {
          return result.openStream();
        } catch (IOException e) {
          throw new NotFoundException("Failed to open class " + classname, e);
        }
      }

      public URL find(String classname) {
        return rootLoader.getResource(classname.replace('.', '/') + ".class");
      }
    });

    instance = this;

    this.logger = LogManager.getLogger(LabyfyLauncherPlugin.class);
    this.injectedTransformers = MultimapBuilder.treeKeys(Integer::compare).linkedListValues().build();
  }

  @Override
  public String name() {
    return "Labyfy";
  }

  @Override
  public void configureRootLoader(RootClassLoader classloader) {
    classloader.excludeFromModification("javassist.", "com.google.");
  }

  @Override
  public void modifyCommandlineArguments(List<String> arguments) {
    this.launchArguments = arguments;
  }

  @SuppressWarnings("InstantiationOfUtilityClass")
  @Override
  public void preLaunch(ClassLoader launchClassloader) {
    Map<String, String> arguments = new HashMap<>();

    for (Iterator<String> it = launchArguments.iterator(); it.hasNext(); ) {
      String key = it.next();
      if (it.hasNext()) {
        arguments.put(key, it.next());
      } else {
        arguments.put(key, null);
      }
    }

    new EntryPoint(arguments);

    // init sentry
    if(logger!=null && (arguments.containsKey("--sentry") && !arguments.get("--sentry").equals("false"))){
      initSentry(arguments);
    }

    try {
      Initializer.boot();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    InjectionHolder.enableIngameState();
  }

  @Override
  public byte[] modifyClass(String className, byte[] classData) {
    for (LateInjectedTransformer transformer : injectedTransformers.values()) {
      byte[] newData = transformer.transform(className, classData);
      if (newData != null) {
        classData = newData;
      }
    }

    try {
      CtClass ctClass =
          ClassPool.getDefault().makeClass(new ByteArrayInputStream(classData), false);


      CtConstructor initializer = ctClass.getClassInitializer();
      if (initializer == null) {
        initializer = ctClass.makeClassInitializer();
      }

      return ctClass.toBytecode();
    } catch (IOException e) {
      throw new RuntimeException("Failed to modify class due to IOException", e);
    } catch (CannotCompileException e) {
      logger.warn("Failed to modify class due to compilation error", e);
      return null;
    }
  }

  public void registerTransformer(int priority, LateInjectedTransformer transformer) {
    injectedTransformers.put(priority, transformer);
  }

  /**
   * Search for manifest.mf entries for labyfy
   * @param name Name of the requested entry
   * @return Value for requested entry
   */
  private String findManifestEntry(String name) {
    try {
      Enumeration<URL> resources = Thread.currentThread().getContextClassLoader()
              .getResources("META-INF/MANIFEST.MF");
      while (resources.hasMoreElements()) {
        URL manifestUrl = resources.nextElement();
        Manifest manifest = new Manifest(manifestUrl.openStream());
        Attributes mainAttributes = manifest.getMainAttributes();
        String implementationTitle = mainAttributes.getValue("Implementation-Title");
        if (implementationTitle != null && implementationTitle.equals("labyfy")) {
          return mainAttributes.getValue(name);
        }
      }
    } catch (Exception e){
      logger.error(e);
    }
    return "unknown";
  }

  /**
   * Client sending errors to sentry
   * @param arguments the launchArguments map
   */
  private void initSentry(Map<String, String> arguments) {
    logger.info("Initializing Sentry");
    // manifest entries are set in the main build.grade file
    String dsn = findManifestEntry("Sentry-dsn");
    String version = findManifestEntry("Implementation-Version");
    String environment = "PRODUCTION";

    if (arguments.containsKey("--debug") && arguments.get("--debug").equals("true")){
      environment = "DEVELOPMENT";
    }

    // sentry project id 2
    Sentry.init(
            "https://" + dsn + "@sentry.labymod.net/2?" +
                    "release=" + version + "&" +
                    "environment=" + environment
    );

    if (arguments.containsKey("--debug") && arguments.get("--debug").equals("true")){
      Sentry.getContext().recordBreadcrumb(
              new BreadcrumbBuilder().setMessage("User started with development enviroment").build()
      );
    }

  }

}
