package net.flintmc.mcapi.gamesettings.settings;

/** An enumeration representing points of view. */
public enum PointOfView {
  FIRST_PERSON(true, false),
  THIRD_PERSON_BACK(false, false),
  THIRD_PERSON_FRONT(false, true);

  private final boolean firstPerson;
  private final boolean mirrored;

  PointOfView(boolean firstPerson, boolean mirrored) {
    this.firstPerson = firstPerson;
    this.mirrored = mirrored;
  }

  public boolean isFirstPerson() {
    return firstPerson;
  }

  public boolean isMirrored() {
    return mirrored;
  }
}