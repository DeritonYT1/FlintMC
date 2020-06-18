package net.labyfy.component.inject.implement;

import com.google.inject.Singleton;
import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.InjectionServiceShare;
import net.labyfy.component.inject.ServiceRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

import static net.labyfy.base.structure.AutoLoadPriorityConstants.*;

@Singleton
@Service(Implement.class)
@AutoLoad(priority = IMPLEMENT_SERVICE_PRIORITY, round = IMPLEMENT_SERVICE_ROUND)
public class ImplementService extends InjectionServiceShare implements ServiceHandler {

  private final Map<String, String> launchArguments;


  @Inject
  private ImplementService(@Named("launchArguments") Map launchArguments) {
    this.launchArguments = launchArguments;
  }

  public void discover(Identifier.Base property) {
    Class<?> location = property.getProperty().getLocatedIdentifiedAnnotation().getLocation();
    Implement annotation = property.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();

    if (!annotation.value().isAssignableFrom(location)) return;

    if (!(annotation.version().isEmpty()
        || launchArguments.get("--version").equals(annotation.version()))) return;

    implementations.put(annotation.value(), location);

  }

  @AutoLoad(priority = IMPLEMENT_SERVICE_REGISTRAR_PRIORITY)
  public static class Registrar{
    static {
      ServiceRepository.addPriorityService("net.labyfy.component.inject.implement.ImplementService");
    }
  }
}