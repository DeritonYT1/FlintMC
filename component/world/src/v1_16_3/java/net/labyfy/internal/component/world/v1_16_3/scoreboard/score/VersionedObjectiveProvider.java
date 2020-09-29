package net.labyfy.internal.component.world.v1_16_3.scoreboard.score;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.scoreboad.score.Criteria;
import net.labyfy.component.world.scoreboad.score.Objective;
import net.labyfy.component.world.scoreboad.type.RenderType;

/**
 * 1.16.3 implementation of {@link Objective.Provider}
 */
@Singleton
@Implement(value = Objective.Provider.class, version = "1.16.3")
public class VersionedObjectiveProvider implements Objective.Provider {

  private final Objective.Factory objectiveFactory;

  @Inject
  public VersionedObjectiveProvider(Objective.Factory objectiveFactory) {
    this.objectiveFactory = objectiveFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Objective get(
          Scoreboard scoreboard,
          String name,
          ChatComponent displayName,
          Criteria criteria,
          RenderType type
  ) {
    return this.objectiveFactory.create(scoreboard, name, displayName, criteria, type);
  }

}