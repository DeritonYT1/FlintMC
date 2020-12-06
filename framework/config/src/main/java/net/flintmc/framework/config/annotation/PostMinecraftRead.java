package net.flintmc.framework.config.annotation;

import net.flintmc.framework.eventbus.event.subscribe.Subscribe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used along with {@link Config} so that the config will be read from the
 * storages after the {@link Subscribe.Phase#POST} phase of the {@link MinecraftInitializeEvent}.
 *
 * <p>If this annotation is not provided, the config will directly be read after it has been
 * discovered.
 *
 * @see Config
 * @see PostOpenGLRead
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PostMinecraftRead {}
