package net.labyfy.component.i18n.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.i18n.GenericResourceBundle;
import net.labyfy.component.i18n.Localizer;
import net.labyfy.component.i18n.internal.UTF8Control;
import net.labyfy.component.inject.implement.Implement;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Labys implementation of {@link Localizer}
 */
@Singleton
@Implement(value = Localizer.class , version = "1.15.2")
public class VersionedLocalizer implements Localizer {

    private final Logger logger;
    private final GenericResourceBundle currValues;

    @Inject
    public VersionedLocalizer(Logger logger, GenericResourceBundle bundle){
        this.logger = logger;
        this.currValues = bundle;
        load();
    }

    /**
     * Loading the save way
     */
    private void load() {
        // First add all keys in eng
        currValues.addJavaResourceBundle(ResourceBundle.getBundle("messages", Locale.ENGLISH, new UTF8Control()));

        // then get the lang and update the keys for the lang
        // leaving the non-specific set to eng
        Locale locale = this.getLanguage();
        if (!locale.getLanguage().equals(Locale.ENGLISH.getLanguage()) && localeAvailable(locale)) {
            currValues.overrideAll(ResourceBundle.getBundle("messages", locale, new UTF8Control()));
        }
    }

    /**
     * Get the current Minecraft language
     * @return respective locale
     */
    private Locale getLanguage() {
        return new Locale(Minecraft.getInstance().getLanguageManager().getCurrentLanguage().getCode().split("_")[0]);
    }

    /**
     * Checking if lang was explicit marked ready
     * @param lang Lang to check
     * @return Language is loadable
     */
    private boolean localeAvailable(Locale lang) {
        String language = lang.getLanguage();
        return language.equals("de") || language.equals("en");
    }

    /**
     * {@inheritDoc}
     */
    public boolean reload(){
        Locale locale = this.getLanguage();
        if(!localeAvailable(locale)) {
            return false;
        }
        // First add all keys in eng
        // then get the lang and update the keys for the lang
        // leaving the non-specific set to eng
        currValues.overrideAll(ResourceBundle.getBundle("messages", Locale.ENGLISH, new UTF8Control()));
        currValues.overrideAll(ResourceBundle.getBundle("messages", locale, new UTF8Control()));

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public String dsp(String key){
        if (!currValues.getString(key).isEmpty()) {
            return currValues.getString(key);
        } else {
            logger.warn("Dispatcher key not found" + key);
            return key;
        }
    }
}