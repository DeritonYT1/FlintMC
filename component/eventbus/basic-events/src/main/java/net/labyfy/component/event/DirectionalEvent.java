package net.labyfy.component.event;

import net.labyfy.component.eventbus.event.filter.EventGroup;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The base event for communication between two applications (e.g. a client and server) with a specifiable direction
 * (sending, receiving). The {@link DirectionFilter} can be applied to methods subscribed to this event.
 *
 * @see Subscribe
 */
public interface DirectionalEvent {

  /**
   * Retrieves the direction in which this event has happened.
   *
   * @return The non-null direction of the action of this event
   */
  Direction getDirection();

  /**
   * An enumeration with all possible directions for the {@link DirectionalEvent}.
   */
  enum Direction {

    /**
     * The direction for receiving something from somewhere else (e.g. a server).
     */
    RECEIVE,
    /**
     * The direction for sending something to somewhere else (e.g. a server).
     */
    SEND

  }

  /**
   * The {@link EventGroup} annotation to filter {@link DirectionalEvent}s by their direction.
   *
   * @see EventGroup
   */
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @EventGroup(groupEvent = DirectionalEvent.class)
  @interface DirectionFilter {

    /**
     * Retrieves the direction to match the direction in the {@link DirectionalEvent}.
     *
     * @return The non-null direction
     * @see DirectionalEvent#getDirection()
     */
    Direction value();

  }

}
