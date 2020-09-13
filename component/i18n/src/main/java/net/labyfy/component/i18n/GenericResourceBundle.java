package net.labyfy.component.i18n;

import java.util.ResourceBundle;

/**
 * Represents a generic resource bundle for key value
 */
public interface GenericResourceBundle {

  /**
   * Adding all key to this bundle without override.
   *
   * @param bundle Bundle to add
   */
  void addJavaResourceBundle(ResourceBundle bundle);

  /**
   * Add data if key not present.
   *
   * @param key Key to change
   * @param val Value to set
   */
  void addData(String key, String val);

  /**
   * Override all keys from this bundle.
   *
   * @param bundle Bundle with keys to override
   */
  void overrideAll(ResourceBundle bundle);

  /**
   * Retrieves the text for a language key.
   *
   * @param key Key to get
   * @return The translated String if available, {@code null} otherwise
   */
  String getString(String key);

}
