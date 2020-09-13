package net.labyfy.component.i18n.v1_15_2;

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

    private final Localizer dsp;
    private final Logger logger;

    @Inject
    public VersionedLocalizerReloadListener(Localizer dsp, Logger logger){
        this.dsp = dsp;
        this.logger = logger;
    }

    @Hook(
            executionTime = {Hook.ExecutionTime.AFTER},
            className = "net.minecraft.client.resources.LanguageManager",
            methodName = "setCurrentLanguage",
            parameters = @Type(reference = Language.class)
    )
    public void reload() {
        logger.info("Laby language reload was" + (dsp.reload() ? "successful!": "unsuccessful :("));
    }

}
