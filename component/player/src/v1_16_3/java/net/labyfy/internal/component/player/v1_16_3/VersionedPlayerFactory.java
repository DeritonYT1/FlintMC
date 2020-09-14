package net.labyfy.internal.component.player.v1_16_3;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.Player;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;

/**
 * 1.16.3 implementation of {@link Player.Factory}
 */
@Implement(value = Player.Factory.class, version = "1.16.3")
public class VersionedPlayerFactory implements Player.Factory<AbstractClientPlayerEntity> {
  /**
   * {@inheritDoc}
   */
  @Override
  public Player create(AbstractClientPlayerEntity player) {
    Player playerEntity = InjectionHolder.getInjectedInstance(Player.class);
    playerEntity.setPlayer(player);
    return playerEntity;
  }
}
