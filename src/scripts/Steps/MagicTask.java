package scripts.Steps;

import dax.walker.utils.camera.DaxCamera;
import org.tribot.api.Clicking;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Magic;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.sdk.Log;
import scripts.Data.Const;
import scripts.NpcChat;
import scripts.QuestSteps.NPCStep;
import scripts.Tasks.Priority;
import scripts.Tasks.Task;
import scripts.Timer;
import scripts.Utils;

public class MagicTask implements Task {


    NPCStep magicTalkStep = new NPCStep("Magic Instructor",
            new RSTile(3141, 3087, 0));

    NPCStep magicFinalTalkStep = new NPCStep("Magic Instructor",
            new RSTile(3141, 3087, 0), new String[]{
            "yes.", "No, I'm not planning to do that."});


    public void attackChicken() {
        if (!Magic.isSpellSelected())
            Magic.selectSpell("Wind Strike");

        RSNPC[] chicken = NPCs.findNearest("Chicken");
        if (chicken.length > 0 && Magic.isSpellSelected()) {
            Log.log("[Debug]: Attacking Chicken");
            if (!chicken[0].isClickable())
                DaxCamera.focus(chicken[0]);

            if (chicken[0].click("Cast")) {
                Timer.waitCondition(() -> Game.getSetting(281) == 670, 4200, 6000);
            }
        }

    }

    public void openMagicTab() {
        if (GameTab.open(GameTab.TABS.MAGIC))
            Timer.slowWaitCondition(() -> GameTab.getOpen() ==
                    GameTab.TABS.MAGIC, 1250, 2000);
    }


    @Override
    public String toString() {
        return "Magic Task";
    }


    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return Game.getSetting(Const.GAME_SETTING) >= 610 &&
                Game.getSetting(Const.GAME_SETTING) < 1000;
    }

    @Override
    public void execute() {
        if (Game.getSetting(Const.GAME_SETTING) == 610 || Game.getSetting(Const.GAME_SETTING) == 620) {
            magicTalkStep.execute();
        }
       else if (Game.getSetting(Const.GAME_SETTING) == 630) {
            openMagicTab();
        }
        else if (Game.getSetting(Const.GAME_SETTING) == 640) {
            magicTalkStep.execute();
        }
        else if (Game.getSetting(Const.GAME_SETTING) == 650) {
            attackChicken();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 670) {
            magicFinalTalkStep.execute();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 1000) {
            // printStats();
        }
    }
}
