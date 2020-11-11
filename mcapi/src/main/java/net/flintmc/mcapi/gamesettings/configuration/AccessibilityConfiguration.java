package net.flintmc.mcapi.gamesettings.configuration;

import net.flintmc.mcapi.gamesettings.settings.PointOfView;
import net.flintmc.mcapi.gamesettings.settings.TutorialSteps;

/** Represents the accessibility configuration. */
public interface AccessibilityConfiguration {

  /**
   * Whether automatic jumping is activated.
   *
   * @return {@code true} if automatic jumping is activated, otherwise {@code false}.
   */
  boolean isAutoJump();

  /**
   * Changes the state for the auto jump.
   *
   * @param autoJump The new state for the auto jump.
   */
  void setAutoJump(boolean autoJump);

  /**
   * Whether the crouch is toggled.
   *
   * @return {@code true} if the crouch is toggled, otherwise {@code false}.
   */
  boolean isToggleCrouch();

  /**
   * Changes the state for the toggle crouch.
   *
   * @param toggleCrouch The new state for toggle crouch.
   */
  void setToggleCrouch(boolean toggleCrouch);

  /**
   * Whether the sprint is toggled.
   *
   * @return {@code true} if the sprint is toggled, otherwise {@code false}.
   */
  boolean isToggleSprint();

  /**
   * Changes the state for the toggle sprint.
   *
   * @param toggleSprint The new state for the toggle sprint.
   */
  void setToggleSprint(boolean toggleSprint);

  /**
   * Whether the server address is hidden.
   *
   * @return {@code true} if the server address is hidden, otherwise {@code false}.
   */
  boolean isHideServerAddress();

  /**
   * Changes the state to hide the server address.
   *
   * @param hideServerAddress The new state to hide the server address.
   */
  void setHideServerAddress(boolean hideServerAddress);

  /**
   * Whether the advanced item tooltips is activated.
   *
   * @return {@code true} if the advanced item tooltips is activated.
   */
  boolean isAdvancedItemTooltips();

  /**
   * Changes the state for the advanced item tooltips.
   *
   * @param advancedItemTooltips The new state for the advanced item tooltips.
   */
  void setAdvancedItemTooltips(boolean advancedItemTooltips);

  /**
   * Whether the focus should be lost during the pause.
   *
   * @return {@code true} if the focus should be lost during the pause, otherwise {@code false}.
   */
  boolean isPauseOnLostFocus();

  /**
   * Changes the state to lost the focus on pause.
   *
   * @param pauseOnLostFocus The new state for the pause.
   */
  void setPauseOnLostFocus(boolean pauseOnLostFocus);

  /**
   * Whether it is a smooth camera.
   *
   * @return {@code true} if it is a smooth camera, otherwise {@code false}.
   */
  boolean isSmoothCamera();

  /**
   * Changes the state of the smooth camera.
   *
   * @param smoothCamera The new state for the smooth camera.
   */
  void setSmoothCamera(boolean smoothCamera);

  /**
   * Whether to display tooltips for held objects.
   *
   * @return {@code true} if tooltips for held objects is displayed, otherwise {@code false}.
   */
  boolean isHeldItemTooltips();

  /**
   * Changes the state for held item tooltips.
   *
   * @param heldItemTooltips The new state for held item tooltips.
   */
  void setHeldItemTooltips(boolean heldItemTooltips);

  /**
   * Whether the native transport can be used.
   *
   * @return {@code true} if the native transport can be used, otherwise {@code false}.
   */
  boolean isUseNativeTransport();

  /**
   * Changes whether the native transport should be used.
   *
   * @param useNativeTransport The new state for the native transport.
   */
  void setUseNativeTransport(boolean useNativeTransport);

  /**
   * Retrieves the tutorial steps.
   *
   * @return The tutorial steps.
   */
  TutorialSteps getTutorialStep();

  /**
   * Changes the tutorial steps.
   *
   * @param tutorialStep The new tutorial steps.
   */
  void setTutorialStep(TutorialSteps tutorialStep);

  /**
   * Whether the snooper is activated.
   *
   * @return {@code true} if the snooper is activated, otherwise {@code false}.
   */
  boolean isSnooper();

  /**
   * Changes the state for the snooper.
   *
   * @param snooper The new snooper state.
   */
  void setSnooper(boolean snooper);

  /**
   * Whether the multiplayer warning can be skipped.
   *
   * @return {@code true} if the multiplayer warning can be skipped.
   */
  boolean isSkipMultiplayerWarning();

  /**
   * Changes the state for skipping the multiplayer warning.
   *
   * @param skipMultiplayerWarning The new state for skipping the multiplayer warning.
   */
  void setSkipMultiplayerWarning(boolean skipMultiplayerWarning);

  /**
   * Retrieves the point of view.
   *
   * @return The point of view.
   */
  PointOfView getPointOfView();

  /**
   * Changes the point of view.
   *
   * @param pointOfView The new point of view.
   */
  void setPointOfView(PointOfView pointOfView);

  /**
   * Retrieves the last server.
   *
   * @return The last server.
   */
  String getLastServer();

  /**
   * Changes the last server.
   *
   * @param lastServer The new last server.
   */
  void setLastServer(String lastServer);

  /**
   * Retrieves the current language.
   *
   * @return The current language.
   */
  String getLanguage();

  /**
   * Changes the language.
   *
   * @param language The new language.
   */
  void setLanguage(String language);

  /**
   * Whether chunks are written synchronously.
   *
   * @return {@code true} if chunks are written synchronously, otherwise {@code false}.
   */
  boolean isChunkSyncWrites();

  /**
   * Changes of the state for chunks are written synchronously.
   *
   * @param chunkSyncWrites The new state.
   */
  void setChunkSyncWrites(boolean chunkSyncWrites);
}