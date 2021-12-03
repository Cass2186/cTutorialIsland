package scripts.Steps;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.Data.Const;
import scripts.PathingUtil;
import scripts.QuestSteps.NPCStep;
import scripts.Tasks.Priority;
import scripts.Tasks.Task;
import scripts.Timer;
import scripts.Utils;

public class SurvivalGuide implements Task {

    NPCStep guide = new NPCStep("Survival Expert", Const.SURVIVAL_AREA);

    int TINDERBOX = 590;
    int MIND_RUNE = 558;
    int BRONZE_AXE = 1351;
    int LOGS = 2511;

    public void openInventoryTab() {
        final int INVENTORY_INTERFACE = General.isClientResizable() ? 164 : 548;
        RSInterface inv = Interfaces.findWhereAction("Inventory", INVENTORY_INTERFACE);
        if (inv != null && inv.click()) {
            Timer.slowWaitCondition(() -> GameTab.getOpen() ==
                    GameTab.TABS.INVENTORY, 1250, 2000);
        }
    }

    public void fish() {
        if (Utils.clickNPC("Fishing spot", "Net"))
            Timer.abc2WaitCondition(() -> Inventory.find("Raw shrimps").length > 0,
                    15000, 17000);

    }

    public void openSkillsTab() {
        int SKILLS_INTERFACE = General.isClientResizable() ? 164 : 548;
        RSInterface skills = Interfaces.findWhereAction("Skills", SKILLS_INTERFACE);
        if (skills != null && skills.click()) {
            Timer.slowWaitCondition(() -> GameTab.getOpen() ==
                    GameTab.TABS.STATS, 1250, 2000);
        }
    }

    public void chopTree() {
        if (Utils.clickObject("Tree", "Chop down", false))
            Timer.abc2WaitCondition(() -> Inventory.find("Logs").length > 0, 14000,
                    17500);
    }

    public void lightFire() {
        RSTile pos = Player.getPosition();
        RSObject[] fire = Objects.findNearest(1, "Fire");
        if (fire.length > 0 && fire[0].getPosition().equals(pos) &&
                Walking.blindWalkTo(Player.getPosition().translate(-1, -1))) {
            PathingUtil.movementIdle();
        } else if (Utils.useItemOnItem(Const.TINDERBOX, Const.LOGS)) {
            Timer.abc2WaitCondition(() -> !Player.getPosition().equals(pos), 14000, 17500);
        }
    }


    public void cookShrimps() {
        RSObject[] fire = Objects.findNearest(10, "Fire");
        if (fire.length < 1) {
            chopTree();
            lightFire();
        } else if (Utils.useItemOnObject(Const.RAW_SHRIMPS, Const.FIRE_ID)) {
            Timer.abc2WaitCondition(() -> Inventory.find("Shrimps").length > 0,
                    14000, 17500);
        }
    }

    @Override
    public String toString() {
        return "Survival Guide Task (" + Game.getSetting(281) + ")";
    }

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public boolean validate() {
        return Game.getSetting(Const.GAME_SETTING) >= 10 && Game.getSetting(Const.GAME_SETTING) < 100;
    }

    @Override
    public void execute() {
        if (Game.getSetting(Const.GAME_SETTING) == 10 || Game.getSetting(Const.GAME_SETTING) == 20) {
            guide.execute();
        } else if (Game.getSetting(Const.GAME_SETTING) == 30) {
            openInventoryTab();
        } else if (Game.getSetting(Const.GAME_SETTING) == 40) {
            fish();
        } else if (Game.getSetting(Const.GAME_SETTING) == 50) {
            openSkillsTab();
        } else if (Game.getSetting(Const.GAME_SETTING) == 60) {
            guide.execute();
        } else if (Game.getSetting(Const.GAME_SETTING) == 70) {
            chopTree();
        } else if (Game.getSetting(Const.GAME_SETTING) == 80) {
            lightFire();
        } else if (Game.getSetting(Const.GAME_SETTING) == 90) {
            cookShrimps();
        }
    }
}
