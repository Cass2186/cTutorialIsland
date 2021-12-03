package scripts.Steps;

import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.sdk.Waiting;
import scripts.Data.Const;
import scripts.QuestSteps.NPCStep;
import scripts.Tasks.Priority;
import scripts.Tasks.Task;

public class QuestGuide implements Task {
    NPCStep talkToQuestGuide = new NPCStep("Quest Guide", new RSTile(3086, 3122, 0));


    public void openQuestTab() {
        int parent = General.isClientResizable() ? 164 : 548;
        RSInterface interfaceQuest = Interfaces.findWhereAction("Quest List", parent);
        if (interfaceQuest != null && interfaceQuest.click())
            Waiting.waitNormal(750, 275);

    }

    @Override
    public String toString() {
        return "Quest Guide";
    }


    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return Game.getSetting(Const.GAME_SETTING) >= 170 &&
                Game.getSetting(Const.GAME_SETTING) <=240;
    }

    @Override
    public void execute() {
        if (Game.getSetting(Const.GAME_SETTING) == 170 || Game.getSetting(Const.GAME_SETTING) == 200 ||
                Game.getSetting(Const.GAME_SETTING) == 220) {
            talkToQuestGuide.execute();
        }
        else   if (Game.getSetting(Const.GAME_SETTING) == 230) {
            openQuestTab();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 240) {
            talkToQuestGuide.execute();
        }
    }

}
