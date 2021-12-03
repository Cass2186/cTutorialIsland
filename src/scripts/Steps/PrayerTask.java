package scripts.Steps;

import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSTile;
import scripts.Data.Const;
import scripts.QuestSteps.NPCStep;
import scripts.Tasks.Priority;
import scripts.Tasks.Task;
import scripts.Timer;

public class PrayerTask implements Task {

    NPCStep brotherBraceStep = new NPCStep(  "Brother Brace",   new RSTile(3124, 3106, 0));

    private void openPrayerTab() {
        int SKILLS_INTERFACE = General.isClientResizable() ? 164 : 548;
        RSInterface skills = Interfaces.findWhereAction("Prayer", SKILLS_INTERFACE);
        if (skills != null && skills.click()) {
            Timer.slowWaitCondition(() -> GameTab.getOpen() ==
                    GameTab.TABS.PRAYERS, 1250, 2000);
        }
    }

    private void openFriendsTab() {
        int SKILLS_INTERFACE = General.isClientResizable() ? 164 : 548;
        RSInterface skills = Interfaces.findWhereAction("Friends List", SKILLS_INTERFACE);
        if (skills != null && skills.click()) {
            Timer.slowWaitCondition(() -> GameTab.getOpen() ==
                    GameTab.TABS.FRIENDS, 1250, 2000);
        }
    }

    @Override
    public String toString() {
        return "Prayer Task";
    }

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return Game.getSetting(Const.GAME_SETTING) >= 540 &&
                Game.getSetting(Const.GAME_SETTING) <= 600;
    }

    @Override
    public void execute() {
        if (Game.getSetting(Const.GAME_SETTING) == 540 || Game.getSetting(Const.GAME_SETTING) == 550) {
           brotherBraceStep.execute();
        }
        else if (Game.getSetting(Const.GAME_SETTING) == 560) {
            openPrayerTab();
        }
        else if (Game.getSetting(Const.GAME_SETTING) == 570) {
            brotherBraceStep.execute();
        }
        else if (Game.getSetting(Const.GAME_SETTING) == 580) {
            openFriendsTab();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 600) {
            brotherBraceStep.execute();
        }
    }
}
