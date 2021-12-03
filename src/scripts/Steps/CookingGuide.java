package scripts.Steps;

import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSTile;
import scripts.Data.Const;
import scripts.QuestSteps.NPCStep;
import scripts.Tasks.Priority;
import scripts.Tasks.Task;
import scripts.Timer;
import scripts.Utils;

public class CookingGuide implements Task {

    NPCStep talkToCook = new NPCStep("Master Chef", new RSTile(3076, 3084, 0));

    @Override
    public String toString() {
        return "Cooking Guide";
    }

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return Game.getSetting(Const.GAME_SETTING) >= 100 &&
                Game.getSetting(Const.GAME_SETTING) < 170;
    }

    @Override
    public void execute() {
        if (Game.getSetting(Const.GAME_SETTING) >= 100 &&
                Game.getSetting(Const.GAME_SETTING) < 150) {
            talkToCook.execute();
        }
        else   if (Game.getSetting(Const.GAME_SETTING) == 150) {
            if (Utils.useItemOnItem(Const.BUCKET_OF_WATER, Const.POT_OF_FLOUR))
                Timer.slowWaitCondition(() -> Inventory.find("Bread dough").length > 0, 3000, 4500);

        }
        else    if (Game.getSetting(Const.GAME_SETTING) == 160) {
            if (Utils.clickObject("Range", "Cook", false)){
                Timer.abc2WaitCondition(() -> Inventory.find("Bread").length > 0, 3000, 4500);
            }
        }
    }
}
