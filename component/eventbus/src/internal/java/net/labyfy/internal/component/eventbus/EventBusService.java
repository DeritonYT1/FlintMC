package net.labyfy.internal.component.eventbus;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.filter.EventGroup;
import net.labyfy.component.eventbus.event.subscribe.PostSubscribe;
import net.labyfy.component.eventbus.event.subscribe.PreSubscribe;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.eventbus.method.SubscribeMethod;
import net.labyfy.component.service.ServiceLoadException;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.identifier.LocatedIdentifiedAnnotation;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service for sending events to receivers.
 */
@Singleton
@Service(value = {Subscribe.class, PreSubscribe.class, PostSubscribe.class}, priority = -10000)
public class EventBusService implements ServiceHandler {

  private final AtomicReference<Injector> injectorReference;
  private final SubscribeMethod.Factory subscribedMethodFactory;

  private final EventBus eventBus;

  @Inject
  private EventBusService(
          @Named("injectorReference") AtomicReference injectorReference,
          SubscribeMethod.Factory subscribedMethodFactory,
          EventBus eventBus
  ) {
    this.injectorReference = injectorReference;
    this.subscribedMethodFactory = subscribedMethodFactory;
    this.eventBus = eventBus;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(Identifier.Base property) throws ServiceNotFoundException {

    LocatedIdentifiedAnnotation locatedIdentifiedAnnotation = property.getProperty().getLocatedIdentifiedAnnotation();
    Annotation subscribe = locatedIdentifiedAnnotation.getAnnotation();
    Method method = locatedIdentifiedAnnotation.getLocation();

    Class<?> eventClass = method.getParameterTypes()[0];
    Collection<Annotation> groupAnnotations = new ArrayList<>();

    for (Annotation annotation : method.getDeclaredAnnotations()) {
      Class<? extends Annotation> type = annotation.annotationType();
      if (type.isAnnotationPresent(EventGroup.class) &&
              type.getAnnotation(EventGroup.class).groupEvent().isAssignableFrom(eventClass)) {
        groupAnnotations.add(annotation);
      }
    }

    Object instance = this.injectorReference.get().getInstance(method.getDeclaringClass());

    byte priority;
    Subscribe.Phase phase;
    if (subscribe instanceof PreSubscribe) {
      priority = ((PreSubscribe) subscribe).priority();
      phase = Subscribe.Phase.PRE;
    } else if (subscribe instanceof PostSubscribe) {
      priority = ((PostSubscribe) subscribe).priority();
      phase = Subscribe.Phase.POST;
    } else if (subscribe instanceof Subscribe) {
      priority = ((Subscribe) subscribe).priority();
      phase = ((Subscribe) subscribe).phase();
    } else {
      throw new ServiceLoadException("Unknown subscribe annotation: " + subscribe);
    }

    // Initializes a new subscribe method
    SubscribeMethod subscribeMethod = this.subscribedMethodFactory.create(
            priority,
            phase,
            instance,
            method,
            groupAnnotations
    );

    this.eventBus.getSubscribeMethods().put(eventClass, subscribeMethod);
  }

}
