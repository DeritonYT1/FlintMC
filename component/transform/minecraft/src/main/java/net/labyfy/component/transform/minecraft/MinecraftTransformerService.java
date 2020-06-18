package net.labyfy.component.transform.minecraft;

import com.google.inject.Inject;
import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.inject.ServiceRepository;
import net.labyfy.component.transform.launchplugin.LabyfyLauncherPlugin;
import net.labyfy.component.transform.launchplugin.LateInjectedTransformer;

import javax.inject.Singleton;

import static net.labyfy.base.structure.AutoLoadPriorityConstants.*;

@Singleton
@Service(value = MinecraftTransformer.class, priority = -10)
@AutoLoad(priority = MINECRAFT_TRANSFORMER_SERVICE_PRIORITY, round = MINECRAFT_TRANSFORMER_SERVICE_ROUND)
public class MinecraftTransformerService implements ServiceHandler {


  @Inject
  private MinecraftTransformerService() {
  }

  public void discover(Identifier.Base property) {
    if (LateInjectedTransformer.class.isAssignableFrom(property.getProperty().getLocatedIdentifiedAnnotation().getLocation())) {
      LabyfyLauncherPlugin.getInstance()
          .registerTransformer(
              property
                  .getProperty()
                  .getLocatedIdentifiedAnnotation()
                  .<MinecraftTransformer>getAnnotation()
                  .priority(),
              InjectionHolder.getInjectedInstance(
                  property
                      .getProperty()
                      .getLocatedIdentifiedAnnotation()
                      .<Class<? extends LateInjectedTransformer>>getLocation()));
    }

  }

  @AutoLoad(priority = MINECRAFT_TRANSFORMER_SERVICE_REGISTRAR_PRIORITY)
  public static class Registrar {
    static {
      ServiceRepository.addPriorityService("net.labyfy.component.transform.minecraft.MinecraftTransformerService");
    }
  }
}
