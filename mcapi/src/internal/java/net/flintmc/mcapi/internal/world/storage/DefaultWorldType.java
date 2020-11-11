package net.flintmc.mcapi.internal.world.storage;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.type.WorldType;

/**
 * Default implementation of the {@link WorldType}.
 */
@Implement(WorldType.class)
public class DefaultWorldType implements WorldType {

  private final int identifier;
  private final String name;
  private final String serializedIdentifier;
  private final int version;
  private boolean canBeCreated;
  private boolean versioned;
  private boolean hasInfoNotice;
  private boolean customConfiguration;

  @AssistedInject
  private DefaultWorldType(
          @Assisted("identifier") int identifier,
          @Assisted("name") String name) {
    this(identifier, name, name, 0);
  }

  @AssistedInject
  private DefaultWorldType(
          @Assisted("identifier") int identifier,
          @Assisted("name") String name,
          @Assisted("version") int version) {
    this(identifier, name, name, version);
  }

  @AssistedInject
  private DefaultWorldType(
          @Assisted("identifier") int identifier,
          @Assisted("name") String name,
          @Assisted("serializedIdentifier") String serializedIdentifier,
          @Assisted("version") int version) {
    this(identifier, name, serializedIdentifier, version, true, false, false, false);
  }

  @AssistedInject
  private DefaultWorldType(
          @Assisted("identifier") int identifier,
          @Assisted("name") String name,
          @Assisted("serializedIdentifier") String serializedIdentifier,
          @Assisted("version") int version,
          @Assisted("canBeCreated") boolean canBeCreated,
          @Assisted("versioned") boolean versioned,
          @Assisted("hasInfoNotice") boolean hasInfoNotice,
          @Assisted("customConfiguration") boolean customConfiguration) {
    this.identifier = identifier;
    this.name = name;
    this.serializedIdentifier = serializedIdentifier;
    this.version = version;
    this.canBeCreated = canBeCreated;
    this.versioned = versioned;
    this.hasInfoNotice = hasInfoNotice;
    this.customConfiguration = customConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSerializationIdentifier() {
    return this.serializedIdentifier;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getVersion() {
    return this.version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasCustomConfigurations() {
    return this.customConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCustomConfigurations(boolean customConfigurations) {
    this.customConfiguration = customConfigurations;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeCreated() {
    return this.canBeCreated;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCanBeCreated(boolean canBeCreated) {
    this.canBeCreated = canBeCreated;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isVersioned() {
    return this.versioned;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void enableVersioned() {
    this.versioned = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getIdentifier() {
    return this.identifier;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasInfoNotice() {
    return this.hasInfoNotice;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void enabledInfoNotice() {
    this.hasInfoNotice = true;
  }
}