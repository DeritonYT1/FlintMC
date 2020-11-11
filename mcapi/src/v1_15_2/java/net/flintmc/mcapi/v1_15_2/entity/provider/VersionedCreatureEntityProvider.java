package net.flintmc.mcapi.v1_15_2.entity.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.CreatureEntity;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;

/** 1.15.2 implementation of the {@link CreatureEntity.Provider}. */
@Singleton
@Implement(value = CreatureEntity.Provider.class, version = "1.15.2")
public class VersionedCreatureEntityProvider implements CreatureEntity.Provider {

  private final CreatureEntity.Factory creatureEntityFactory;
  private final EntityTypeMapper entityTypeMapper;

  @Inject
  private VersionedCreatureEntityProvider(
      CreatureEntity.Factory creatureEntityFactory, EntityTypeMapper entityTypeMapper) {
    this.creatureEntityFactory = creatureEntityFactory;
    this.entityTypeMapper = entityTypeMapper;
  }

  /** {@inheritDoc} */
  @Override
  public CreatureEntity get(Object entity) {
    if (!(entity instanceof net.minecraft.entity.CreatureEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.CreatureEntity.class.getName());
    }
    net.minecraft.entity.CreatureEntity creatureEntity =
        (net.minecraft.entity.CreatureEntity) entity;

    return this.creatureEntityFactory.create(
        creatureEntity, this.entityTypeMapper.fromMinecraftEntityType(creatureEntity.getType()));
  }
}