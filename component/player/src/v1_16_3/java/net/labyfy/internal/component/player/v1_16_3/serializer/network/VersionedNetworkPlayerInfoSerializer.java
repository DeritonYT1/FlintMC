package net.labyfy.internal.component.player.v1_16_3.serializer.network;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.serializer.network.NetworkPlayerInfoSerializer;

/**
 * 1.16.3 implementation of {@link NetworkPlayerInfoSerializer}
 */
@Singleton
@Implement(value = NetworkPlayerInfoSerializer.class, version = "1.16.3")
public class VersionedNetworkPlayerInfoSerializer implements NetworkPlayerInfoSerializer<net.minecraft.client.network.play.NetworkPlayerInfo> {

  /**
   * Deserializes the Mojang {@link net.minecraft.client.network.play.NetworkPlayerInfo}
   * to the Labyfy {@link NetworkPlayerInfo}
   *
   * @param value The network player info being deserialized
   * @return A deserialized {@link NetworkPlayerInfo}
   */
  @Override
  public NetworkPlayerInfo deserialize(net.minecraft.client.network.play.NetworkPlayerInfo value) {
    return InjectionHolder.getInjectedInstance(NetworkPlayerInfo.class);
  }

  /**
   * Serializes the Labyfy {@link NetworkPlayerInfo}
   * to the {@link net.minecraft.client.network.play.NetworkPlayerInfo}
   *
   * @param value The network player info being serialized
   * @return A serialized {@link net.minecraft.client.network.play.NetworkPlayerInfo}
   */
  @Override
  @Deprecated
  public net.minecraft.client.network.play.NetworkPlayerInfo serialize(NetworkPlayerInfo value) {
    throw new UnsupportedOperationException("The method is unsupported.");
  }
}
