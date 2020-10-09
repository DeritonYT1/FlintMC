package net.labyfy.internal.component.eventbus.method;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Key;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.name.Names;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.eventbus.method.SubscribeMethod;
import net.labyfy.component.inject.InjectedInvocationHelper;
import net.labyfy.component.inject.implement.Implement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * A subscribed method in an {@link EventBus}.
 */
@Implement(SubscribeMethod.class)
public class DefaultSubscribeMethod implements SubscribeMethod {

  private final InjectedInvocationHelper injectedInvocationHelper;
  private final byte priority;
  private final Subscribe.Phase phase;
  private final Object instance;
  private final Method eventMethod;
  private final Collection<Annotation> groupAnnotations;

  @AssistedInject
  private DefaultSubscribeMethod(
      InjectedInvocationHelper injectedInvocationHelper,
      @Assisted("priority") byte priority,
      @Assisted("phase") Subscribe.Phase phase,
      @Assisted("instance") Object instance,
      @Assisted("eventMethod") Method eventMethod,
      @Assisted("groupAnnotations") Collection<Annotation> groupAnnotations
  ) {
    this.injectedInvocationHelper = injectedInvocationHelper;
    this.priority = priority;
    this.phase = phase;
    this.instance = instance;
    this.eventMethod = eventMethod;
    this.groupAnnotations = groupAnnotations;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte getPriority() {
    return this.priority;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Method getEventMethod() {
    return this.eventMethod;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Subscribe.Phase getPhase() {
    return this.phase;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<Annotation> getGroupAnnotations() {
    return this.groupAnnotations;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void invoke(Object event) throws Throwable {
    this.injectedInvocationHelper.invokeMethod(this.eventMethod, this.instance, ImmutableMap.of(
        Key.get(Object.class, Names.named("event")), event,
        Key.get(Subscribe.Phase.class), phase
    ));
  }

}
