package net.labyfy.component.assistedinject;

import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;

import javax.inject.Singleton;

@Singleton
@Service(AssistedFactory.class)
public class AssistedFactoryService implements ServiceHandler {

  public void discover(Identifier.Base property) {
    System.out.println("Discovered");
  }
}
