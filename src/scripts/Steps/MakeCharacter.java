package scripts.Steps;

import org.tribot.api.General;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSTile;
import scripts.Data.Const;
import scripts.InterfaceUtil;
import scripts.QuestSteps.NPCStep;
import scripts.Tasks.Priority;
import scripts.Tasks.Task;
import scripts.Timer;
import scripts.Utils;

public class MakeCharacter implements Task {

    int PARENT_INDEX_NAMING = 558;
    int PARENT_INDEX_STYLING = 679;
    String greenCol = "<col=00ff00>";
    String redCol = "<col=ff0000>";


    // game setting = 0
    public void nameCharacter() {
        RSInterface nameBox = Interfaces.findWhereAction("Enter name", PARENT_INDEX_NAMING);
        if (nameBox != null) {
            General.println("[Debug]: Typing Name");
            int i = General.random(0, Const.NAMES.length - 1);
            Keyboard.typeString(Const.NAMES[i]);
            Keyboard.pressEnter();
            Timer.waitCondition(() -> Interfaces.isInterfaceSubstantiated(PARENT_INDEX_NAMING, 14), 9000, 13000);
        }
        RSInterfaceChild goodNameText = Interfaces.get(PARENT_INDEX_NAMING, 13);
        if (goodNameText != null) {
            if (goodNameText.getText().contains(redCol)) {
                General.println("[Debug]: Name is NOT available");
                RSInterfaceChild nameSuggestionOne = Interfaces.get(PARENT_INDEX_NAMING, 15);
                if (nameSuggestionOne != null && nameSuggestionOne.click()) {
                    //selects suggested name

                }
            }
            if (goodNameText.getText().contains(greenCol)) {
                General.println("[Debug]: Name is available");
                RSInterface confirmNameButton = Interfaces.get(PARENT_INDEX_NAMING, 19, 9);
                if (confirmNameButton != null && confirmNameButton.click()) //confirms name
                    Timer.waitCondition(() -> !Interfaces.isInterfaceSubstantiated(PARENT_INDEX_NAMING), 9000, 13000);
            }
        }
    }

    public void styleCharacter() {
        if (Interfaces.isInterfaceSubstantiated(PARENT_INDEX_STYLING)) {
            General.println("[Debug]: Styling character");
            selectFeatures(General.random(12, 13));
            selectFeatures(General.random(16, 17));
            selectFeatures(General.random(20, 21));
            selectFeatures(General.random(43, 44));
            selectFeatures(General.random(47, 48));
        }

        General.sleep(General.randomSD(1750, 550));

        RSInterface confirmBox = Interfaces.findWhereAction("Confirm", PARENT_INDEX_STYLING);
        if (confirmBox != null && confirmBox.click()) {
            Timer.waitCondition(() -> Game.getSetting(280) != 0, 6000, 8000);
        }
    }


    public void selectFeatures(int child) {
        int a = General.random(1, 8);
        for (int b = 0; b < a; b++) {
            General.sleep(General.randomSD(100, 620, 280, 90));
            RSInterface inter = Interfaces.get(PARENT_INDEX_STYLING, child);
            if (inter != null)
                inter.click();
        }
    }

    NPCStep guide = new NPCStep("Gielinor Guide", new RSTile(3094, 3107, 0));

    public void gielinorGuide() {

        guide.addDialogStep("I am an experienced player.");
        guide.execute();

    }

    public boolean openSettingsTab() {
        if (InterfaceUtil.clickInterfaceAction(164, "Settings"))
            return Timer.abc2WaitCondition(() -> GameTab.getOpen() == GameTab.TABS.OPTIONS, 3500, 5000);
        return false;
    }

    @Override
    public String toString() {
        return "Gielinor Guide Task";
    }

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public boolean validate() {
        return Game.getSetting(Const.GAME_SETTING) < 10;
    }

    @Override
    public void execute() {
        if (Game.getSetting(Const.GAME_SETTING) == 1) {
            styleCharacter();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 2) {
            gielinorGuide();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 3) {
            openSettingsTab();
        }
        else if (Game.getSetting(Const.GAME_SETTING) == 7) {
            gielinorGuide();
            Utils.setCameraZoomAboveDefault();
        }
    }
}
