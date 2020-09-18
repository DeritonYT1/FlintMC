package net.labyfy.internal.component.player.v1_15_2.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.Player;
import net.labyfy.component.player.PlayerSkinProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.serializer.util.GameModeSerializer;
import net.labyfy.component.player.serializer.util.SkinModelSerializer;
import net.labyfy.component.player.util.GameMode;
import net.labyfy.component.player.util.SkinModel;
import net.labyfy.component.resources.ResourceLocationProvider;

/**
 * 1.15.2 implementation of the {@link NetworkPlayerInfo.Provider}
 */
@Singleton
@Implement(value = NetworkPlayerInfo.Provider.class, version = "1.15.2")
public class VersionedNetworkPlayerInfoProvider implements NetworkPlayerInfo.Provider {

  private final GameModeSerializer<GameMode> gameModeGameModeSerializer;
  private final NetworkPlayerInfo.Factory networkPlayerInfoFactory;
  private final PlayerSkinProfile.Factory playerSkinProfileFactory;
  private final ResourceLocationProvider resourceLocationProvider;
  private final SkinModelSerializer<SkinModel> skinModelSkinModelSerializer;

  @Inject
  public VersionedNetworkPlayerInfoProvider(
          GameModeSerializer gameModeGameModeSerializer,
          NetworkPlayerInfo.Factory networkPlayerInfoFactory,
          PlayerSkinProfile.Factory playerSkinProfileFactory,
          ResourceLocationProvider resourceLocationProvider,
          SkinModelSerializer skinModelSkinModelSerializer
  ) {
    this.gameModeGameModeSerializer = gameModeGameModeSerializer;
    this.networkPlayerInfoFactory = networkPlayerInfoFactory;
    this.playerSkinProfileFactory = playerSkinProfileFactory;
    this.resourceLocationProvider = resourceLocationProvider;
    this.skinModelSkinModelSerializer = skinModelSkinModelSerializer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NetworkPlayerInfo get(Player player) {
    return this.networkPlayerInfoFactory.create(
            player,
            this.gameModeGameModeSerializer,
            this.resourceLocationProvider,
            this.skinModelSkinModelSerializer,
            this.playerSkinProfileFactory
    );
  }

}
