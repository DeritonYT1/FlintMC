package net.flintmc.mcapi.settings.flint.annotation.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;

/**
 * Defines {@link ApplicableSetting}s in a {@link Config} to be sub settings of another {@link
 * ApplicableSetting}.
 *
 * <p>The class where the with {@link SubSettingsFor} annotated interface is located has to be
 * either an inner class of the class where the parent setting is defined or {@link #declaring()}
 * has to return the class where the parent setting is defined.
 *
 * <p>Example of a parent setting with sub settings:
 *
 * <pre>
 *
 * {@literal @}BooleanSetting
 * boolean getSomeParent();
 *
 * SubSettings getSubSettings();
 *
 * {@literal @}SubSettingsFor("SomeParent")
 * interface SubSettings {
 *
 *   {@literal @}StringSetting
 *   String getSomeSubSetting();
 *
 * }
 *
 * </pre>
 *
 * <p>This would mark everything in the SubSettings interface as a sub setting of getParent.
 *
 * <p>If a setting is already a sub setting of a setting, no other settings can be the sub setting
 * of this setting. Sub settings cannot have sub settings.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubSettingsFor {

  /**
   * Retrieves the name of the parent. If the getter is 'getX', the name would be 'X'
   *
   * @return The name of the parent
   * @see Config
   */
  String value();

  /**
   * Retrieves the declaring class of the parent. If this is not provided, the declaring class of
   * the class where this annotation is located will be used.
   *
   * @return The optional declaring class
   */
  Class<?> declaring() default Dummy.class;

  /**
   * Dummy for {@link #declaring()} to mark that no class is defined.
   */
  final class Dummy {

    private Dummy() {
    }
  }
}
