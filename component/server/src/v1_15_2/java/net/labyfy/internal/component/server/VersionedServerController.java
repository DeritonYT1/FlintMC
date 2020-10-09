package net.labyfy.internal.component.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.server.ConnectedServer;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.ServerController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.*;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.text.TranslationTextComponent;

@Singleton
@AutoLoad(round = 11) // round after shadow
@Implement(value = ServerController.class, version = "1.15.2")
public class VersionedServerController implements ServerController {

  private final ConnectedServer connectedServer;

  @Inject
  public VersionedServerController(ConnectedServer connectedServer) {
    this.connectedServer = connectedServer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isConnected() {
    return this.connectedServer.isConnected();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isConnecting() {
    return this.connectedServer.isConnecting();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConnectedServer getConnectedServer() {
    return this.isConnected() || this.isConnecting() ? this.connectedServer : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disconnectFromServer() {
    if (Minecraft.getInstance().world != null) {
      // rendering has to be called from Minecraft's main thread
      Minecraft.getInstance().execute(this::disconnectNow);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void connectToServer(ServerAddress address) {
    // rendering has to be called from Minecraft's main thread
    Minecraft.getInstance().execute(() -> {
      this.disconnectNow();

      Screen currentScreen = Minecraft.getInstance().currentScreen;
      Minecraft.getInstance().displayGuiScreen(new ConnectingScreen(currentScreen, Minecraft.getInstance(), address.getIP(), address.getPort()));
    });
  }

  private void disconnectNow() {
    if (Minecraft.getInstance().world == null) {
      return;
    }

    boolean integrated = Minecraft.getInstance().isIntegratedServerRunning();
    boolean realms = Minecraft.getInstance().isConnectedToRealms();
    Minecraft.getInstance().world.sendQuittingDisconnectingPacket();

    if (integrated) {
      Minecraft.getInstance().unloadWorld(new DirtMessageScreen(new TranslationTextComponent("menu.savingLevel")));
    } else {
      Minecraft.getInstance().unloadWorld();
    }

    if (integrated) {
      Minecraft.getInstance().displayGuiScreen(new MainMenuScreen());
    } else if (realms) {
      RealmsBridge bridge = new RealmsBridge();
      bridge.switchToRealms(new MainMenuScreen());
    } else {
      Minecraft.getInstance().displayGuiScreen(new MultiplayerScreen(new MainMenuScreen()));
    }
  }
}
