package net.flintmc.mcapi.gamesettings.configuration;

import java.util.List;

/** Represents the resource pack configuration. */
public interface ResourcePackConfiguration {

  /**
   * Retrieves a collection with all resource packs.<br>
   * <b>Note:</b> If you use this method to add a resource pack, the client only knows about it and
   * is temporary not saved in the options. The next time the options are saved, the added resource
   * packs are also saved.
   *
   * @return A collection with all resource packs.
   */
  List<String> getResourcePacks();

  /**
   * Changes the old resource pack collection with the new collection.
   *
   * @param resourcePacks The new resource pack collection.
   */
  void setResourcePacks(List<String> resourcePacks);

  /**
   * Retrieves a collection with all incompatible resource packs.<br>
   * <b>Note:</b> If you use this method to add an incompatible resource pack, the client only knows
   * about it and is temporary not saved in the options. The next time the options are saved, the
   * added incompatible resource packs are also saved.
   *
   * @return A collection with all incompatible resource packs.
   */
  List<String> getIncompatibleResourcePacks();

  /**
   * Changes the old incompatible resource pack collection with the new collection.
   *
   * @param incompatibleResourcePacks The new incompatible resource pack colleciton.
   */
  void setIncompatibleResourcePacks(List<String> incompatibleResourcePacks);
}