package net.labyfy.component.i18n.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.i18n.GenericResourceBundle;
import net.labyfy.component.inject.implement.Implement;
import org.apache.logging.log4j.Logger;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * A default implementation of {@link GenericResourceBundle}
 */
@Singleton
@Implement(value = GenericResourceBundle.class)
public class DefaultGenericResourceBundle implements GenericResourceBundle {

  private final Map<String, String> resources;
  private final Logger logger;

  @Inject
  public DefaultGenericResourceBundle(Logger logger) {
    this.logger = logger;
    this.resources = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addJavaResourceBundle(ResourceBundle bundle) {
    Enumeration<String> keys = bundle.getKeys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      addData(key, bundle.getString(key));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addData(String key, String val) {
    key = key.toLowerCase();
    if (this.resources.containsKey(key)) {
      this.logger.warn("Duplicated key '" + key + "'");
    }
    this.resources.put(key, val);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void overrideAll(ResourceBundle bundle) {
    for (String key : bundle.keySet()){
      this.resources.put(key,bundle.getString(key));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getString(String key) {
    if (key == null || key.isEmpty()) {
      return "";
    }
    return this.resources.get(key.toLowerCase());
  }
}
