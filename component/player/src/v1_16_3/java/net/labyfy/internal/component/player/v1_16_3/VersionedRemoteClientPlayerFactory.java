package net.labyfy.internal.component.player.v1_16_3;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.RemoteClientPlayer;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;

/**
 * 1.16.3 implementation of {@link RemoteClientPlayer.Factory}
 */
@Implement(value = RemoteClientPlayer.Factory.class, version = "1.16.3")
public class VersionedRemoteClientPlayerFactory implements RemoteClientPlayer.Factory<AbstractClientPlayerEntity> {

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteClientPlayer create(AbstractClientPlayerEntity player) {
    RemoteClientPlayer remoteClientPlayer = InjectionHolder.getInjectedInstance(RemoteClientPlayer.class);
    remoteClientPlayer.setPlayer(player);
    return remoteClientPlayer;
  }
}
