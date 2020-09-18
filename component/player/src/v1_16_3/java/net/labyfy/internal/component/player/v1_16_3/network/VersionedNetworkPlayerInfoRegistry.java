package net.labyfy.internal.component.player.v1_16_3.network;

import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.network.NetworkPlayerInfoRegistry;
import net.labyfy.component.player.serializer.network.NetworkPlayerInfoSerializer;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * 1.16.3 implementation of {@link NetworkPlayerInfoRegistry}
 */
@Implement(value = NetworkPlayerInfoRegistry.class, version = "1.16.3")
public class VersionedNetworkPlayerInfoRegistry implements NetworkPlayerInfoRegistry {

    private final NetworkPlayerInfoSerializer<net.minecraft.client.network.play.NetworkPlayerInfo> networkPlayerInfoSerializer;

    @Inject
    public VersionedNetworkPlayerInfoRegistry(NetworkPlayerInfoSerializer networkPlayerInfoSerializer) {
        this.networkPlayerInfoSerializer = networkPlayerInfoSerializer;
    }

    /**
     * Retrieves the network info of a player with the username
     *
     * @param username The username of a player
     * @return The network info of a player
     */
    @Override
    public NetworkPlayerInfo getPlayerInfo(String username) {
        return this.networkPlayerInfoSerializer.deserialize(Minecraft.getInstance().getConnection().getPlayerInfo(username));
    }

    /**
     * Retrieves the network info of a player with the unique identifier
     *
     * @param uniqueId The unique identifier of a player
     * @return The network info of a player
     */
    @Override
    public NetworkPlayerInfo getPlayerInfo(UUID uniqueId) {
        return this.networkPlayerInfoSerializer.deserialize(Minecraft.getInstance().getConnection().getPlayerInfo(uniqueId));
    }

    /**
     * Retrieves a collection of a network player info
     *
     * @return A collection of {@link NetworkPlayerInfo}
     */
    @Override
    public Collection<NetworkPlayerInfo> getPlayerInfo() {
        Collection<NetworkPlayerInfo> collection = new ArrayList<>();
        for (net.minecraft.client.network.play.NetworkPlayerInfo playerInfo : Minecraft.getInstance().getConnection().getPlayerInfoMap()) {
            NetworkPlayerInfo networkPlayerInfo = this.networkPlayerInfoSerializer.deserialize(playerInfo);
            collection.add(networkPlayerInfo);
        }

        return collection;
    }

}
