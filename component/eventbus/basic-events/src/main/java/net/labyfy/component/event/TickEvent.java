package net.labyfy.component.event;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.eventbus.event.filter.EventGroup;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This event will be fired every tick. It will be fired in both PRE and POST phases. The {@link TickFilter} can be
 * applied to methods subscribed to this event.
 *
 * @see Subscribe
 */
public interface TickEvent {

  /**
   * Retrieves the type of the tick in this event.
   *
   * @return The non-null type
   */
  Type getType();

  /**
   * An enumeration of the states in the tick.
   */
  enum Type {

    /**
     * This type will be fired at the beginning and the ending of one full tick.
     */
    GENERAL,
    /**
     * This type will be fired at the beginning and the ending of one game render tick, which means that it will only be
     * fired when the user is in game and doesn't have paused the game.
     */
    GAME_RENDER,
    /**
     * This type will be fired at the beginning and the ending of one world render tick (directly after the GAME_RENDER
     * tick), which means that it will only be fired when the user is in game and doesn't have paused the game.
     */
    WORLD_RENDER

  }

  /**
   * Factory for the {@link TickEvent}.
   */
  @AssistedFactory(TickEvent.class)
  interface Factory {

    /**
     * Creates a new {@link TickEvent} with the given type.
     *
     * @param type The non-null type of the new event
     * @return The new non-nul event
     */
    TickEvent create(@Assisted("type") Type type);

  }

  /**
   * The {@link EventGroup} annotation to filter {@link TickEvent}s by their type.
   *
   * @see EventGroup
   */
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @EventGroup(groupEvent = TickEvent.class)
  @interface TickFilter {

    /**
     * Retrieves the type to match the type in the given {@link TickEvent}.
     *
     * @return The non-null type
     * @see TickEvent#getType()
     */
    Type value();

  }

}
