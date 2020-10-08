package net.labyfy.internal.component.entity.v1_15_2.type;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntitySize;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.ClientWorld;

@Implement(value = EntityType.class, version = "1.15.2")
public class VersionedEntityType implements EntityType {

  private final Entity.Factory entityFactory;
  private final Entity.Classification classification;
  private final boolean serializable;
  private final boolean summonable;
  private final boolean immuneToFire;
  private final boolean canSpawnFarFromPlayer;
  private final EntitySize entitySize;

  @AssistedInject
  private VersionedEntityType(
          @Assisted("entityFactory") Entity.Factory entityFactory,
          @Assisted("classification") Entity.Classification classification,
          @Assisted("serializable") boolean serializable,
          @Assisted("summonable") boolean summonable,
          @Assisted("immuneToFire") boolean immuneToFire,
          @Assisted("canSpawnFarFromPlayer") boolean canSpawnFarFromPlayer,
          @Assisted("entitySize") EntitySize entitySize
  ) {
    this.entityFactory = entityFactory;
    this.classification = classification;
    this.serializable = serializable;
    this.summonable = summonable;
    this.immuneToFire = immuneToFire;
    this.canSpawnFarFromPlayer = canSpawnFarFromPlayer;
    this.entitySize = entitySize;
  }

  @Override
  public Entity.Classification getClassification() {
    return this.classification;
  }

  @Override
  public boolean isSerializable() {
    return this.serializable;
  }

  @Override
  public boolean isSummonable() {
    return this.summonable;
  }

  @Override
  public boolean isImmuneToFire() {
    return this.immuneToFire;
  }

  @Override
  public boolean canSpawnFarFromPlayer() {
    return this.canSpawnFarFromPlayer;
  }

  @Override
  public EntitySize getSize() {
    return this.entitySize;
  }

}
