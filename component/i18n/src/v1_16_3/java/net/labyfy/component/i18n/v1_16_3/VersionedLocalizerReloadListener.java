package net.labyfy.component.i18n.v1_16_3;

import com.google.inject.Inject;
import net.labyfy.component.i18n.Localizer;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.minecraft.client.resources.Language;
import org.apache.logging.log4j.Logger;

/**
 * Listener for changing language in minecraft
 */
@AutoLoad
public class VersionedLocalizerReloadListener {

  private final Localizer localizer;
  private final Logger logger;

  @Inject
  public VersionedLocalizerReloadListener(Localizer localizer, Logger logger) {
    this.localizer = localizer;
    this.logger = logger;
  }

  @Hook(
          executionTime = Hook.ExecutionTime.AFTER,
          className = "net.minecraft.client.resources.LanguageManager",
          methodName = "setCurrentLanguage",
          parameters = @Type(reference = Language.class)
  )
  public void reload() {
    this.logger.info("Labyfy language reload was " + (this.localizer.reload() ? "successful!" : "unsuccessful :("));
  }

}
