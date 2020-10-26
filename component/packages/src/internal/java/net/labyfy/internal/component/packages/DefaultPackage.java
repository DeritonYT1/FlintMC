package net.labyfy.internal.component.packages;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javassist.ClassPool;
import javassist.NotFoundException;
import net.labyfy.component.inject.InjectionService;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.packages.Package;
import net.labyfy.component.packages.PackageClassLoader;
import net.labyfy.component.packages.PackageManifest;
import net.labyfy.component.packages.PackageState;
import net.labyfy.component.packages.localization.PackageLocalizationLoader;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.processing.autoload.DetectableAnnotationProvider;
import net.labyfy.component.processing.autoload.identifier.ClassIdentifier;
import net.labyfy.component.service.ExtendedServiceLoader;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceRepository;
import net.labyfy.component.stereotype.service.Services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;

/**
 * Default implementation of the {@link Package}.
 */
@Implement(Package.class)
public class DefaultPackage implements Package {

  private final ServiceRepository serviceRepository;
  private final File jarFile;

  private PackageLocalizationLoader localizationLoader;
  private PackageManifest packageManifest;
  private PackageState packageState;
  private PackageClassLoader classLoader;
  private Exception loadException;

  /**
   * Creates a new Labyfy package with the given description loader and the
   * given files.
   *
   * @param serviceRepository The singleton instance of the {@link ServiceRepository}
   * @param manifestLoader The loader to use for reading the manifest
   * @param jarFile        The java IO file this package should be loaded from, or null if loaded from the classpath
   * @param jar            The java IO jar file this package should be loaded from, must point to the same file as
   *                       the `file` parameter, or must be null if the package has been loaded from the classpath
   */
  @AssistedInject
  private DefaultPackage(
          ServiceRepository serviceRepository,
          PackageLocalizationLoader localizationLoader,
          DefaultPackageManifestLoader manifestLoader,
          @Assisted File jarFile,
          @Assisted JarFile jar) {
    this.jarFile = jarFile;

    if (jar != null) {
      // If the package should be loaded from a jar file, try to retrieve the manifest from it
      if (!manifestLoader.isManifestPresent(jar)) {
        throw new IllegalArgumentException(
            "The given JAR file " + jarFile.getName() + " does not contain a package manifest");
      }

      // Try to load the manifest
      this.packageState = PackageState.INVALID_MANIFEST;
      PackageManifest manifest;

      try {
        manifest = manifestLoader.loadManifest(jar);

        if (manifest.isValid()) {
          this.packageState = PackageState.NOT_LOADED;
        }

        this.packageManifest = manifest;

        // Try to load localizations
        this.localizationLoader = localizationLoader;
        if (localizationLoader.isLanguageFolderPresent(jar)) {
          localizationLoader.loadLocalizations(jar);
        }

      } catch (IOException ignored) {
      }

    } else {
      // The package should not be loaded from a file, thus we don't have a manifest and mark  the
      // package
      // as ready for load
      this.packageState = PackageState.NOT_LOADED;
    }
  }

  /** {@inheritDoc} */
  @Override
  public PackageManifest getPackageManifest() {
    return this.packageManifest;
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return this.packageManifest != null ? this.packageManifest.getName() : jarFile.getName();
  }

  /** {@inheritDoc} */
  @Override
  public String getDisplayName() {
    return this.packageManifest != null ? this.packageManifest.getDisplayName() : jarFile.getName();
  }

  /** {@inheritDoc} */
  @Override
  public String getVersion() {
    return this.packageManifest != null ? this.packageManifest.getVersion() : "unknown";
  }

  /** {@inheritDoc} */
  @Override
  public PackageState getState() {
    return this.packageState;
  }

  /** {@inheritDoc} */
  @Override
  public void setState(PackageState state) {
    if (packageState != PackageState.NOT_LOADED) {
      throw new IllegalStateException(
          "The package state can only be changed if the package has not been loaded already");
    } else if (state == PackageState.LOADED) {
      throw new IllegalArgumentException("The package state can't be set to LOADED explicitly, use the load() method");
    }

    this.packageState = state;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getFile() {
    return this.jarFile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PackageState load() {
    if (packageState != PackageState.NOT_LOADED) {
      throw new IllegalStateException("The package has to be in the NOT_LOADED state in order to be loaded");
    }

    try {
      this.classLoader = new DefaultPackageClassLoader(this);
      this.packageState = PackageState.LOADED;
    } catch (Exception exception) {
      // The package failed to load, save the error and mark the package itself as errored
      this.packageState = PackageState.ERRORED;
      this.loadException = exception;
    }
    return this.packageState;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void enable() {
    if (packageState != PackageState.LOADED) {
      throw new IllegalStateException("The package has to be in the LOADED state in order to be enabled");
    }

    try {
      // Find all services on the classpath and register them to the service repository
      this.prepareServices();
      // Flush all registered services that are defined in the PRE_INIT state. This should only be
      // done if it is really necessary.
      this.serviceRepository.flushServices(Service.State.PRE_INIT);
      // Apply all Implementations and AssistedFactories
      InjectionHolder.getInjectedInstance(InjectionService.class).flush();
      // Flush all other higher level framework features like Events, Transforms etc.
      this.serviceRepository.flushServices(Service.State.POST_INIT);
    } catch (NotFoundException e) {
      e.printStackTrace();
    }

    // The package is now enabled
    this.packageState = PackageState.ENABLED;
  }


  private void prepareServices() throws NotFoundException {
    // Find all autoload providers within the package
    List<AnnotationMeta> annotations = getAnnotationMeta();
    // Iterate over all annotations
    for (AnnotationMeta annotationMeta : annotations) {
      if (annotationMeta.getAnnotation().annotationType().equals(Service.class)) {
        // if yes go ahead and register it
        Service annotation = (Service) annotationMeta.getAnnotation();
        serviceRepository.registerService(
            annotation.value(),
            annotation.priority(),
            annotation.state(),
            ClassPool.getDefault()
                .get(((ClassIdentifier) (annotationMeta.getIdentifier())).getName()));
        // if not check if it might be multiple services at once. the Javapoet framework sadly seems
        // to not support Repeatable annotations yet. Maybe this will change sometime.
      } else if (annotationMeta.getAnnotation().annotationType().equals(Services.class)) {
        // Iterate over all services and register them
        for (Service service : ((Services) annotationMeta.getAnnotation()).value()) {
          serviceRepository.registerService(
              service.value(),
              service.priority(),
              service.state(),
              ClassPool.getDefault()
                  .get(((ClassIdentifier) (annotationMeta.getIdentifier())).getName()));
        }
      }
    }

    // Iterate over all annotations again
    for (AnnotationMeta annotationMeta : annotations) {
      // register the annotation
      serviceRepository.registerAnnotation(annotationMeta);
    }
  }


  /**
   * @return all saved annotation meta that was written to the {@link DetectableAnnotationProvider}
   * on compile time
   */
  private List<AnnotationMeta> getAnnotationMeta() {
    List<AnnotationMeta> annotationMetas = new ArrayList<>();

    Set<DetectableAnnotationProvider> discover =
        ExtendedServiceLoader.get(DetectableAnnotationProvider.class)
            .discover(getPackageClassLoader().asClassLoader());
    discover.forEach(
        detectableAnnotationProvider -> detectableAnnotationProvider.register(annotationMetas));
    return annotationMetas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PackageClassLoader getPackageClassLoader() {
    if (packageState != PackageState.LOADED && packageState != PackageState.ENABLED) {
      throw new IllegalStateException(
          "The package has to be in the LOADED or ENABLED state in order to retrieve "
              + "the class loader of it");
    }

    return this.classLoader;
  }

  /** {@inheritDoc} */
  @Override
  public PackageLocalizationLoader getPackageLocalizationLoader() {
    return this.localizationLoader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Exception getLoadException() {
    if (packageState != PackageState.ERRORED) {
      throw new IllegalStateException(
          "The package has to be in the ERRORED state in order to retrieve the exception "
              + "which caused its loading to fail");
    }

    return this.loadException;
  }
}
