package net.flintmc.mcapi.settings.game.configuration;

import net.flintmc.framework.config.annotation.ConfigExclude;
import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.game.annotation.ResourcePackSetting;

import java.util.List;

/** Represents the resource pack configuration. */
@ImplementedConfig
public interface ResourcePackConfiguration {

  /**
   * Retrieves a collection with all resource packs.<br>
   * <b>Note:</b> If you use this method to add a resource pack, the client only knows about it and
   * is temporary not saved in the options. The next time the options are saved, the added resource
   * packs are also saved.
   *
   * @return A collection with all resource packs.
   */
  @ResourcePackSetting
  @DisplayName(@Component(value = "options.resourcepack", translate = true))
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
  @ConfigExclude
  List<String> getIncompatibleResourcePacks();

  /**
   * Changes the old incompatible resource pack collection with the new collection.
   *
   * @param incompatibleResourcePacks The new incompatible resource pack collection.
   */
  @ConfigExclude
  void setIncompatibleResourcePacks(List<String> incompatibleResourcePacks);
}