package net.labyfy.component.i18n.v1_16_3;

import com.google.inject.Inject;
import net.labyfy.component.i18n.GenericResourceBundle;
import net.labyfy.component.i18n.Localizer;
import net.labyfy.component.i18n.internal.UTF8Control;
import net.labyfy.component.inject.implement.Implement;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 1.16.3 labyfy implementation of {@link Localizer}
 */
@Implement(value = Localizer.class, version = "1.16.3")
public class VersionedLocalizer implements Localizer {

  private final Logger logger;
  private final GenericResourceBundle bundle;

  @Inject
  public VersionedLocalizer(
          Logger logger,
          GenericResourceBundle bundle
  ) {
    this.logger = logger;
    this.bundle = bundle;
    this.load();
  }

  private void load() {
    // First add all keys in english
    this.bundle.addJavaResourceBundle(ResourceBundle.getBundle("messages", Locale.ENGLISH, new UTF8Control()));

    // Then get the lang and update the keys for the lang
    // leaving the non-specific set to eng
    Locale locale = this.getLanguage();
    if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage()) && localeAvailable(locale)) {
      this.bundle.overrideAll(ResourceBundle.getBundle("messages", locale, new UTF8Control()));
    }
  }

  /**
   * Retrieves the current Minecraft language.
   *
   * @return The respective locale
   */
  private Locale getLanguage() {
    return new Locale(Minecraft.getInstance().getLanguageManager().getCurrentLanguage().getCode().split("_")[0]);
  }

  /**
   * Checking if language was explicit marked ready.
   *
   * @param locale The language to check
   * @return {@code true} if the Language is loadable, {@code false} otherwise
   */
  private boolean localeAvailable(Locale locale) {
    String language = locale.getLanguage();
    return language.equals("de") || language.equals("en");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String dsp(String key) {
    if(!this.bundle.getString(key).isEmpty()) {
      return this.bundle.getString(key);
    } else {
      this.logger.warn("Dispatcher key not found: " + key);
      return key;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean reload() {
    Locale locale = this.getLanguage();
    if(!this.localeAvailable(locale)) {
      return false;
    }

    // First add all keys in eng
    // then get the lang and update the keys for the lang
    // leaving the non-specific set to eng
    this.bundle.overrideAll(ResourceBundle.getBundle("messages", Locale.ENGLISH, new UTF8Control()));
    this.bundle.overrideAll(ResourceBundle.getBundle("messages", this.getLanguage(), new UTF8Control()));

    return true;
  }
}
