package net.labyfy.component.server;

/**
 * The controller for the currently connected server.
 */
public interface ServerController {

  /**
   * Retrieves whether the client is connected to any server (in multiplayer).
   *
   * @return {@code true} if the client is connected to a server, {@code false} otherwise
   */
  boolean isConnected();

  /**
   * Retrieves whether the client is currently connecting to a server and didn't succeed yet (in multiplayer).
   *
   * @return {@code true} if the client is connecting to a server, {@code false} otherwise
   */
  boolean isConnecting();

  /**
   * Retrieves the currently connected server, this also works if the client is currently connecting to a server.
   *
   * @return The server or {@code null} if the client isn't connected/connecting with any server
   * @see #isConnected()
   * @see #isConnecting()
   */
  ConnectedServer getConnectedServer();

  /**
   * Disconnects the client from the currently connected server or does nothing when not connected to any server.
   *
   * @see #isConnected()
   */
  void disconnectFromServer();

  /**
   * Connects the client with the given address and disconnects from the server the client is currently connected to.
   *
   * @param address The non-null address of the target server
   */
  void connectToServer(ServerAddress address);

}
