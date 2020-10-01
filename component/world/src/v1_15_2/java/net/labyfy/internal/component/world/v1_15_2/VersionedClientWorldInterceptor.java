package net.labyfy.internal.component.world.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.player.ClientPlayer;
import net.labyfy.component.player.RemoteClientPlayer;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.world.ClientWorld;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.Entity;

/**
 * 1.15.2 implementation of the client world interceptor
 */
@Singleton
@AutoLoad
public class VersionedClientWorldInterceptor {

  private final ClientPlayer clientPlayer;
  private final ClientWorld clientWorld;
  private final RemoteClientPlayer.Provider remoteClientPlayerProvider;

  @Inject
  private VersionedClientWorldInterceptor(
          ClientPlayer clientPlayer,
          ClientWorld clientWorld,
          RemoteClientPlayer.Provider remoteClientPlayerProvider
  ) {
    this.clientPlayer = clientPlayer;
    this.clientWorld = clientWorld;
    this.remoteClientPlayerProvider = remoteClientPlayerProvider;
  }

  @Hook(
          className = "net.minecraft.client.world.ClientWorld",
          methodName = "addPlayer",
          parameters = {
                  @Type(reference = int.class),
                  @Type(reference = AbstractClientPlayerEntity.class)
          }
  )
  public void hookAfterAddPlayer(@Named("args") Object[] args) {
    AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity) args[1];

    if (playerEntity instanceof ClientPlayerEntity) {
      this.clientWorld.addPlayer(this.clientPlayer);
    } else if (playerEntity instanceof RemoteClientPlayerEntity) {
      this.clientWorld.addPlayer(this.remoteClientPlayerProvider.get(playerEntity));
    }

  }

  @Hook(
          className = "net.minecraft.client.world.ClientWorld",
          methodName = "removeEntity",
          parameters = {
                  @Type(reference = Entity.class)
          }
  )
  public void hookAfterRemoveEntity(@Named("args") Object[] args) {
    Entity entity = (Entity) args[0];

    if (entity instanceof AbstractClientPlayerEntity) {
      AbstractClientPlayerEntity playerEntity = (AbstractClientPlayerEntity) entity;
      this.clientWorld.removePlayer(playerEntity.getGameProfile().getId());
    }
  }
}
