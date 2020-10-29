package net.labyfy.component.gamesettings.configuration;

import net.labyfy.component.config.annotation.implemented.ImplementedConfig;
import net.labyfy.component.gamesettings.settings.ChatVisibility;
import net.labyfy.component.gamesettings.settings.NarratorStatus;
import net.labyfy.component.settings.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DefineCategory;
import net.labyfy.component.settings.options.BooleanSetting;
import net.labyfy.component.settings.options.dropdown.EnumDropDownSetting;
import net.labyfy.component.settings.options.numeric.Range;
import net.labyfy.component.settings.options.numeric.SliderSetting;

/**
 * Represents the chat configuration.
 */
@DefineCategory(
    name = "minecraft.settings.chat",
    displayName = @Component(value = "minecraft.settings.chat.display", translate = true),
    description = @Component(value = "minecraft.settings.chat.description", translate = true)
)
@ImplementedConfig
public interface ChatConfiguration {

  /**
   * Retrieves the scale of the chat.
   *
   * @return The chat scale.
   */
  @SliderSetting(value = @Range(max = 100), defaultValue = 100)
  // percent
  double getChatScale();

  /**
   * Changes the scale of the chat.
   *
   * @param chatScale The new scale of the chat.
   */
  void setChatScale(double chatScale);

  /**
   * Retrieves the width of the chat.
   *
   * @return The chat width.
   */
  @SliderSetting(value = @Range(min = 40, max = 320), defaultValue = 320)
  // pixels
  double getChatWidth();

  /**
   * Changes the width of the chat.
   *
   * @param chatWidth The new chat width.
   */
  void setChatWidth(double chatWidth);

  /**
   * Retrieves the max height that the chat is allowed to appear normally.
   *
   * @return The max height that the chat is allowed to appear normally.
   */
  @SliderSetting(value = @Range(min = 20, max = 180), defaultValue = 180)
  // pixels
  double getChatHeightUnfocused();

  /**
   * Changes the max height that the chat is allowed to appear normally.
   *
   * @param chatHeightUnfocused The new unfocused chat height.
   */
  void setChatHeightUnfocused(double chatHeightUnfocused);

  /**
   * Retrieves the max height that the chat is allowed to appear when in focus.
   *
   * @return The max height that the chat is allowed to appear when in focus.
   */
  @SliderSetting(value = @Range(min = 20, max = 180), defaultValue = 180)
  // pixels
  double getChatHeightFocused();

  /**
   * Changes the max height that the chat is allowed to appear when in focus.
   *
   * @param chatHeightFocused The new focused chat height.
   */
  void setChatHeightFocused(double chatHeightFocused);

  /**
   * Retrieves the delay of chat messages.
   *
   * @return The delay of chat messages.
   */
  double getChatDelay();

  /**
   * Changes the chat delay of messages.
   *
   * @param chatDelay The new chat delay.
   */
  void setChatDelay(double chatDelay);

  /**
   * Whether colors are displayed in the chat.
   *
   * @return {@code true} if colors are displayed in the chat, otherwise {@code false}.
   */
  @BooleanSetting(defaultValue = true)
  boolean isChatColor();

  /**
   * Changes whether colors are displayed in the chat.
   *
   * @param chatColor The new state.
   */
  void setChatColor(boolean chatColor);

  /**
   * Whether links can be seen in the chat.
   *
   * @return {@code true} if links can be seen in the chat, otherwise {@code false}.
   */
  @BooleanSetting(defaultValue = true)
  boolean isChatLinks();

  /**
   * Changes whether links can be seen in the chat.
   *
   * @param chatLinks The new state.
   */
  void setChatLinks(boolean chatLinks);

  /**
   * Whether each time a player clicks on a URL, a prompt is forced to appear on the player's screen to ensure
   * that they want to leave Minecraft and go to the website.
   *
   * @return {@code true} if forces a prompt, otherwise {@code false}.
   */
  @BooleanSetting(defaultValue = true)
  boolean isChatLinksPrompt();

  /**
   * Changes whether to force a prompt on the player screen each time a player clicks a URL to ensure
   * that they want to leave Minecraft and go to the website.
   *
   * @param chatLinksPrompt The new state for the links prompt.
   */
  void setChatLinksPrompt(boolean chatLinksPrompt);

  /**
   * Retrieves the visibility of the chat.
   *
   * @return The chat visibility.
   */
  @EnumDropDownSetting(defaultValue = 0 /* FULL */)
  ChatVisibility getChatVisibility();

  /**
   * Changes the visibility of the chat.
   *
   * @param chatVisibility The new chat visibility.
   */
  void setChatVisibility(ChatVisibility chatVisibility);

  /**
   * Retrieves the opacity of the chat.
   *
   * @return The chat opacity.
   */
  @SliderSetting(value = @Range(min = 10, max = 100), defaultValue = 100)
  // percent
  double getChatOpacity();

  /**
   * Changes the opacity of the chat.
   *
   * @param chatOpacity The new chat opacity.
   */
  void setChatOpacity(double chatOpacity);

  /**
   * Retrieves the line spacing between texts.
   *
   * @return The line spacing between texts.
   */
  double getChatLineSpacing();

  /**
   * Changes the chat line spacing between texts.
   *
   * @param chatLineSpacing The new chat line spacing.
   */
  void setChatLineSpacing(double chatLineSpacing);

  /**
   * Retrieves the narrator status.
   *
   * @return The current narrator status.
   */
  @EnumDropDownSetting(defaultValue = 0 /* OFF */)
  NarratorStatus getNarrator();

  /**
   * Changes the narrator status.
   *
   * @param narrator The new narrator status.
   */
  void setNarrator(NarratorStatus narrator);

  /**
   * Whether command suggestions show up or not, if off the player has to press tab to bring them up.
   *
   * @return {@code true} if command suggestions show up, otherwise {@code false}.
   */
  @BooleanSetting
  boolean isAutoSuggestCommands();

  /**
   * Changes whether the commands are suggested automatically.
   *
   * @param autoSuggestCommands The new state for auto suggest commands.
   */
  void setAutoSuggestCommands(boolean autoSuggestCommands);

  /**
   * Whether reduced information should be displayed on the debug screen.
   *
   * @return {@code true} if reduced information should be displayed, otherwise {@code false}.
   */
  @BooleanSetting
  boolean isReducedDebugInfo();

  /**
   * Changes the reduced information.
   *
   * @param reducedDebugInfo The new reduced debug information.
   */
  void setReducedDebugInfo(boolean reducedDebugInfo);

  /**
   * Retrieves the opacity of the background.
   *
   * @return The opacity of the background.
   */
  @SliderSetting(value = @Range(max = 100), defaultValue = 100)
  // pixels
  double getAccessibilityTextBackgroundOpacity();

  /**
   * Changes the opacity of the text background.
   *
   * @param accessibilityTextBackgroundOpacity The new text background opacity.
   */
  void setAccessibilityTextBackgroundOpacity(double accessibilityTextBackgroundOpacity);

  /**
   * Whether the text has a background.
   *
   * @return {@code true} if the text has a background, otherwise {@code false}.
   */
  boolean isAccessibilityTextBackground();

  /**
   * Changes whether the text has a background.
   *
   * @param accessibilityTextBackground The new background state.
   */
  void setAccessibilityTextBackground(boolean accessibilityTextBackground);

}
