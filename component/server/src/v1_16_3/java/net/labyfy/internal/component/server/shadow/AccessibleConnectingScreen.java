package net.labyfy.internal.component.server.shadow;

import net.labyfy.component.transform.shadow.FieldGetter;
import net.labyfy.component.transform.shadow.Shadow;
import net.minecraft.network.NetworkManager;

/**
 * Shadow implementation of the {@link net.minecraft.client.gui.screen.DisconnectedScreen} in minecraft with a public
 * getter to get the message component.
 */
@Shadow("net.minecraft.client.gui.screen.ConnectingScreen")
public interface AccessibleConnectingScreen {

  /**
   * Retrieves the network manager of this screen which is used to connect with a server.
   *
   * @return The non-null network manager of this screen
   */
  @FieldGetter("networkManager")
  NetworkManager getNetworkManager();

}
