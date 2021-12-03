package scripts.Steps;

import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSTile;
import scripts.Data.Const;
import scripts.QuestSteps.NPCStep;
import scripts.Tasks.Priority;
import scripts.Tasks.Task;
import scripts.Timer;
import scripts.Utils;

public class MiningGuide implements Task {

    NPCStep miningInstructor = new NPCStep("Mining Instructor", new RSTile(3080,9505, 0));

    public void mineTin() {
        if (Utils.clickObject(10080, "Mine", false))
            Timer.abc2WaitCondition(() -> Inventory.find("Tin ore").length > 0, 9000,
                    12000);

    }

    public void mineCopper() {
        if (Utils.clickObject(10079, "Mine", false)) {
            Timer.abc2WaitCondition(() -> Inventory.find("Copper ore").length > 0, 9000,
                    12000);
        }
    }

    public void smeltBar() {
        if (Utils.clickObject("Furnace", "Use", false))
            Timer.abc2WaitCondition(() -> Inventory.find("Bronze bar").length > 0, 12000, 14000);
    }

    public void smithDagger() {
        if (Interfaces.get(312, 9) == null && Utils.clickObject("Anvil", "Smith", false)) {
            Timer.slowWaitCondition(() -> (Interfaces.get(312, 9) != null),
                    8000, 1000);

        }
        RSInterface dagger = Interfaces.get(312, 9);
        if (dagger != null && dagger.click())
            Timer.abc2WaitCondition(() -> Inventory.find("Bronze dagger").length > 0, 8000, 10000);

    }

    @Override
    public String toString() {
        return "Mining Guide";
    }


    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return Game.getSetting(Const.GAME_SETTING) >= 250 &&
                Game.getSetting(Const.GAME_SETTING) <= 350;
    }

    @Override
    public void execute() {
        if (Game.getSetting(Const.GAME_SETTING) == 250 || Game.getSetting(Const.GAME_SETTING) == 260) {
            miningInstructor.execute();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 270 || Game.getSetting(Const.GAME_SETTING) == 300) {
            mineTin();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 310) {
            mineCopper();
        }
        else if (Game.getSetting(Const.GAME_SETTING) == 320) {
            smeltBar();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 330) {
            miningInstructor.execute();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 340 || Game.getSetting(Const.GAME_SETTING) == 350) {
             smithDagger();
        }
    }
}
