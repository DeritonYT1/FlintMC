package net.labyfy.internal.component.player.v1_16_3.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.Player;
import net.labyfy.component.player.serializer.world.DimensionSerializer;
import net.labyfy.component.player.world.ClientWorld;
import net.labyfy.component.player.world.Dimension;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 1.16.3 implementation of {@link ClientWorld}
 */
@Singleton
@AutoLoad
@Implement(value = ClientWorld.class, version = "1.16.3")
public class VersionedClientWorld implements ClientWorld {

  private final net.minecraft.client.world.ClientWorld clientWorld;
  private final DimensionSerializer<ResourceLocation> dimensionSerializer;

  private final List<Player> players;

  @Inject
  public VersionedClientWorld(
          DimensionSerializer dimensionSerializer
  ) {
    this.dimensionSerializer = dimensionSerializer;
    this.clientWorld = Minecraft.getInstance().player.worldClient;
    this.players = new ArrayList<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getClientWorld() {
    return this.clientWorld;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getTime() {
    return this.clientWorld.getDayTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPlayerCount() {
    return this.clientWorld.getPlayers().size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean addPlayer(Player player) {
    return this.players.add(player);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeIfPlayer(Predicate<? super Player> filter) {
    return this.players.removeIf(filter);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<Player> getPlayers() {
    return this.players;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int getCountLoadedEntities() {
    return this.clientWorld.getCountLoadedEntities();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension getDimension() {
    return this.dimensionSerializer.deserialize(this.clientWorld.func_234923_W_().func_240901_a_());
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Object getScoreboard() {
    return this.clientWorld.getScoreboard();
  }


}
