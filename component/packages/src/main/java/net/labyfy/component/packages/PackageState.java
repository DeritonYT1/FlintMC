package net.labyfy.component.packages;

/**
 * Represents the possible states of a package.
 */
public enum PackageState {
  /**
   * The packages classes are available for use through the loader of the package,
   * but the package has not been enabled yet.
   */
  LOADED,

  /**
   * The packages initializers have run and its services have been registered.
   */
  ENABLED,

  /**
   * The package has not been loaded yet and may not be interacted with. This is the initial state.
   */
  NOT_LOADED,

  /**
   * The manifest file (package.json) was invalid and caused the package to not load.
   * Packages in this state may not be interacted with.
   */
  INVALID_MANIFEST,

  /**
   * The Labyfy environment hosting the package loader is incompatible, possibly due to a version
   * conflict or side problem. Packages in this state may not be interacted with.
   */
  LABYFY_NOT_COMPATIBLE,

  /**
   * The Minecraft environment hosting Labyfy is incompatible, possibly due to a version conflict
   * or a side problem. Packages in this state may not be interacted with.
   */
  MINECRAFT_NOT_COMPATIBLE,

  /**
   * The currently running environment is not able to provide the required package dependencies,
   * possibly due to a version conflict or dependencies failing to load.
   */
  UNSATISFIABLE_DEPENDENCIES,

  /**
   * The currently running environment has loaded another package which is conflicting with this package.
   * This could mean that the same package has been attempted to be loaded twice or just that another
   * package has been declared to be incompatible.
   */
  CONFLICTING_PACKAGE_LOADED,

  /**
   * An {@link Exception} occurred while loading the package. The {@link Package#getLoadException()} method
   * can be used to retrieve the exception causing the package to fail loading.
   */
  ERRORED;

  /**
   * Checks if the given package is in this state.
   *
   * @param pack The package to check.
   * @return {@code true}, if the packages state matches this state, {@code false} otherwise
   */
  public boolean matches(Package pack) {
    return this.equals(pack.getState());
  }
}
