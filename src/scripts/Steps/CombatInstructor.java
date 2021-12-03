package scripts.Steps;

import dax.walker.utils.camera.DaxCamera;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import scripts.*;
import scripts.Data.Const;
import scripts.QuestSteps.NPCStep;
import scripts.Tasks.Priority;
import scripts.Tasks.Task;

public class CombatInstructor implements Task {

    NPCStep combatStep = new NPCStep(3307, new RSTile(3106, 9505, 0));

    private void openEquipmentTab() {
        if (GameTab.open(GameTab.TABS.EQUIPMENT))
            Timer.slowWaitCondition(() -> GameTab.getOpen() == GameTab.TABS.EQUIPMENT, 2500, 4000);
    }

    private void clickGearButton() {
        if (GameTab.getOpen() != GameTab.TABS.EQUIPMENT)
            openEquipmentTab();

        RSInterface gear = Interfaces.get(387, 2);
        if (gear != null && gear.click()) {
            Timer.waitCondition(() -> Interfaces.get(84) != null, 3500, 4500);
        }
    }

    private void equipDagger() {
        RSItem[] dagger = Inventory.find(Const.BRONZE_DAGGER);
        if (dagger.length > 0 && dagger[0].click("Equip")) {
            Timer.slowWaitCondition(() -> Equipment.isEquipped("Bronze dagger"),
                    4000, 5000);
        }
    }

    private void closeEquipmentWindow() {
        if (InterfaceUtil.clickInterfaceAction(84, "Close"))
            Timer.slowWaitCondition(() -> Interfaces.get(84, 3) == null, 4000, 6000);

    }

    private void equipSwordAndShield() {
        if (Utils.equipItem(Const.BRONZE_SWORD))
            Timer.slowWaitCondition(() -> Equipment.isEquipped(Const.BRONZE_SWORD),
                    4000, 5000);
        if (Utils.equipItem(Const.WOODEN_SHIELD)) {
            Timer.slowWaitCondition(() -> Equipment.isEquipped(Const.WOODEN_SHIELD),
                    4000, 5000);
        }
    }


    private void clickCombatTab() {
        int SKILLS_INTERFACE = General.isClientResizable() ? 164 : 548;
        RSInterface skills = Interfaces.findWhereAction("Combat Options", SKILLS_INTERFACE);
        if (skills != null && skills.click()) {
            Timer.slowWaitCondition(() -> GameTab.getOpen() ==
                    GameTab.TABS.COMBAT, 1250, 2000);
        }
    }

    private void killRat() {
        RSTile center = new RSTile(3104, 9517, 0);
        RSNPC[] rats = NPCs.findNearest("Giant rat");
        if (!Combat.isUnderAttack() && rats.length > 0) {
            PathingUtil.localNavigation(center, 2);
            if (CombatUtil.clickTarget(rats[0]))
                Timer.waitCondition(Combat::isUnderAttack, 2500, 3400);
        }
        if (Timer.waitCondition(Combat::isUnderAttack, 2500, 3400)) {
            Timer.abc2WaitCondition(() -> !Combat.isUnderAttack(), 45000, 60000);
        }

    }

    private boolean equipBowAndArrows() {
        if (Utils.equipItem(Const.SHORTBOW))
            Timer.slowWaitCondition(() -> Equipment.isEquipped(Const.SHORTBOW),
                    2500, 4000);
        if (Utils.equipItem(Const.BRONZE_ARROW)) {
            Timer.slowWaitCondition(() -> Equipment.isEquipped(Const.BRONZE_ARROW),
                    2500, 4000);
        }
        return Equipment.isEquipped(Const.BRONZE_ARROW) && Equipment.isEquipped(Const.SHORTBOW);
    }

    private void equipBowAndAttack() {
        RSNPC[] rats = NPCs.findNearest("Giant rat");
        if (equipBowAndArrows() && rats.length > 0) {
            if (!rats[0].isClickable())
                DaxCamera.focus(rats[0]);

            if (DynamicClicking.clickRSNPC(rats[0], "Attack") &&
                    Timer.waitCondition(() -> rats[0].isInCombat(), 5000))
                Timer.abc2WaitCondition(() -> !Combat.isUnderAttack(), 45000, 60000);
        }
    }


    @Override
    public String toString() {
        return "Combat Instructor Task";
    }

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return Game.getSetting(Const.GAME_SETTING) >= 360 &&
                Game.getSetting(Const.GAME_SETTING) <= 490;
    }

    @Override
    public void execute() {
        if (Game.getSetting(Const.GAME_SETTING) == 360 || Game.getSetting(Const.GAME_SETTING) == 370) {
            combatStep.execute();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 390) {
            openEquipmentTab();
        }
        else if (Game.getSetting(Const.GAME_SETTING) == 400) {
            clickGearButton();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 405) {
            equipDagger();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 410) {
            closeEquipmentWindow();
            combatStep.execute();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 420) {
            equipSwordAndShield();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 430) {
            clickCombatTab();
        }
        else if (Game.getSetting(Const.GAME_SETTING) == 440 ||
                Game.getSetting(Const.GAME_SETTING) == 450 || Game.getSetting(Const.GAME_SETTING) == 460) {
            killRat();
        }
        else if (Game.getSetting(Const.GAME_SETTING) == 470) {
            combatStep.execute();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 480 ||
                Game.getSetting(Const.GAME_SETTING) == 490) {
            equipBowAndAttack();
        }
    }
}
