package net.labyfy.component.stereotype.service;

import net.labyfy.component.stereotype.exceptions.ServiceNotFoundException;
import net.labyfy.component.stereotype.identifier.Identifier;

@FunctionalInterface
public interface ServiceHandler {
  void discover(Identifier.Base property) throws ServiceNotFoundException;
}
