package net.labyfy.component.server;

import net.labyfy.component.server.status.ServerStatus;

import java.util.concurrent.CompletableFuture;

public interface ConnectedServer {

  /**
   * Retrieves the address of the server which the client is connected/connecting to.
   *
   * @return The address of the server or {@code null} if the client is not connected/connecting to any server
   */
  ServerAddress getAddress();

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
   * Uses  {@link net.labyfy.component.server.status.ServerStatusResolver#resolveStatus(ServerAddress)} to get the
   * status of a server for the server list.
   *
   * @return The non-null future which will be completed with the status or {@code null} if the status couldn't be
   * retrieved
   * @throws IllegalStateException If the client is not connected with any server
   */
  CompletableFuture<ServerStatus> resolveStatus() throws IllegalStateException;

  /**
   * Sends a custom payload message to the currently connected server which can be used to communicate with servers for
   * any extra data in the client.
   *
   * @param identifier The non-null identifier of the payload
   * @param payload    The non-null payload for the server
   * @throws IllegalStateException If the client is not connected with any server
   */
  // TODO replace the String with the NamespacedKey
  void sendCustomPayload(String identifier, byte[] payload) throws IllegalStateException;

}
