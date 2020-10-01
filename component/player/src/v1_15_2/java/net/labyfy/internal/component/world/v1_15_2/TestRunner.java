package net.labyfy.internal.component.world.v1_15_2;

import com.google.inject.Inject;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.scoreboad.score.PlayerTeam;

/**
 * @author Robby
 */
@AutoLoad
public class TestRunner {

  private final Scoreboard scoreboard;
  private final PlayerTeam.Provider playerTeamProvider;

  @Inject
  public TestRunner(Scoreboard scoreboard, PlayerTeam.Provider playerTeamProvider) {
    this.scoreboard = scoreboard;
    this.playerTeamProvider = playerTeamProvider;
  }


/*
  @Hook(
          className = "net.minecraft.client.gui.IngameGui",
          methodName = "renderGameOverlay",
          parameters = {
                  @Type(reference = float.class)
          }
  )
  public void renderGameOverlay(@Named("args") Object[] args) {
    try {
      Objective objective = this.scoreboard.getObjectiveInDisplaySlot(1);

      if (objective != null) {
        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> sortedScores = scoreboard.getSortedScores(objective);
        List<Score> list = sortedScores.stream().filter(new Predicate<Score>() {
          @Override
          public boolean test(Score score) {
            return score.getPlayerName() != null && !score.getPlayerName().startsWith("#");
          }
        }).collect(Collectors.toList());

        if (list.size() > 15) {
/*          List<Score> custom = new ArrayList<>();
          for (int i = 0; i < 15; i++) {
            Score score = sortedScores.get(i);
            custom.add(score);
          }

          sortedScores = custom;
  *//*      } else {
          sortedScores = list;
        }

        String displayName = objective.getDisplayName().getUnformattedText();
        int stringWidth = Minecraft.getInstance().fontRenderer.getStringWidth(displayName);
        int maxWidth = stringWidth;

        for (Score sortedScore : sortedScores) {
          PlayerTeam playerTeam = this.playerTeamProvider.get(sortedScore.getPlayerName());
          String playerTeamName = "" + sortedScore.getScorePoints();
          maxWidth = Math.max(maxWidth, Minecraft.getInstance().fontRenderer.getStringWidth(playerTeamName));
        }


        int size = sortedScores.size() * 9;
        int height = Minecraft.getInstance().getMainWindow().getScaledHeight() / 2 + size / 3;
        int width = maxWidth - 3;
        int k = 0;


        for (Score sortedScore : sortedScores) {
          ++k;
          PlayerTeam playerTeam = this.playerTeamProvider.get(sortedScore.getPlayerName());
          String playerTeamName = playerTeam.getName();
          String points = ChatColor.RED + "" + sortedScore.getScorePoints();

          int test = height - k * 9;
          int fixedWidth = Minecraft.getInstance().fontRenderer.getStringWidth("14");
          Minecraft.getInstance().fontRenderer.drawString(playerTeamName, fixedWidth + 3, test, -1);
          Minecraft.getInstance().fontRenderer.drawString(points,
                  2, test, -1);

          if (k == sortedScores.size()) {
            Minecraft.getInstance().fontRenderer.drawString(displayName,
                    (float) (fixedWidth + 3 + maxWidth / 2 - stringWidth / 2), (float) test - 9, -1);
          }

        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    */

}
