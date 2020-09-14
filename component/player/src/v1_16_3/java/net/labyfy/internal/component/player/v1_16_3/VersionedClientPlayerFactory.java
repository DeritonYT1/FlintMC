package net.labyfy.internal.component.player.v1_16_3;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.ClientPlayer;
import net.minecraft.client.Minecraft;

/**
 * 1.16.3 implementation of {@link ClientPlayer.Factory}
 */
@Implement(value = ClientPlayer.Factory.class, version = "1.16.3")
public class VersionedClientPlayerFactory implements ClientPlayer.Factory{

  /**
   * {@inheritDoc}
   */
  @Override
  public ClientPlayer create() {
    ClientPlayer player = InjectionHolder.getInjectedInstance(ClientPlayer.class);
    player.setPlayer(Minecraft.getInstance().player);
    return player;
  }
}
