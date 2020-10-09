package net.labyfy.internal.component.server.event.shadow;

import net.labyfy.component.transform.shadow.FieldGetter;
import net.labyfy.component.transform.shadow.Shadow;

/**
 * Shadow implementation of the Thread in ConnectingScreen#connect(String, int) with public getters for the IP, port and
 * the screen.
 */
@Shadow("net.minecraft.client.gui.screen.ConnectingScreen$1")
public interface ServerConnectThread {

  /**
   * Retrieves the screen where this thread has been started.
   *
   * @return The non-null screen
   */
  @FieldGetter("this$0")
  Object getScreen();

  /**
   * Retrieves the ip where this thread should connect to.
   *
   * @return The non-null ip to connect to
   */
  @FieldGetter("val$ip")
  String getIp();

  /**
   * Retrieves the port where this thread should connect to.
   *
   * @return The port to connect to
   */
  @FieldGetter("val$port")
  int getPort();

}
