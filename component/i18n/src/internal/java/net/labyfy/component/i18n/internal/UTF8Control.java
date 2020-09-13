package net.labyfy.component.i18n.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Own implementation of the ResourceBundle.Control
 * to guarantee utf-8
 */
public class UTF8Control extends ResourceBundle.Control {

    /**
     * Creates a new {@link ResourceBundle} with the given parameters.
     *
     * @param baseName The base name of the resource bundle.
     * @param locale The localization for the resource bundle.
     * @param format The encoding codec for the resource bundle. <b>Note:</b> Is not used.
     * @param loader The class loader for the resource bundle.
     * @param reload {@code true} if the resource bundle should be reloaded, otherwise {@code false}
     * @return The resource bundle instance, or {@code null} if none could be found.
     * @throws IllegalAccessException If the class or its nullary constructor is not accessible.
     * @throws InstantiationException If the instantiation of a class fails for some other reason.
     * @throws IOException If an error occurred when reading resources using any I/O operations.
     */
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {

        // The below is a copy of the default implementation.
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "properties");
        ResourceBundle bundle = null;
        InputStream stream = null;
        if (reload) {
            URL url = loader.getResource(resourceName);
            if (url != null) {
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    connection.setUseCaches(false);
                    stream = connection.getInputStream();
                }
            }
        } else {
            stream = loader.getResourceAsStream(resourceName);
        }
        if (stream != null) {
            try {
                // Only this line is changed to make it to read properties files as UTF-8.
              bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
            } finally {
                stream.close();
            }
        }
        return bundle;
    }
}
