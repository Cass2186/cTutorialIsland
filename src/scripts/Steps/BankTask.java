package scripts.Steps;

import dax.walker_engine.interaction_handling.NPCInteraction;
import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.types.RSInterface;
import org.tribot.script.sdk.Waiting;
import scripts.*;
import scripts.Data.Const;
import scripts.Tasks.Priority;
import scripts.Tasks.Task;

public class BankTask implements Task {

    private void openBank() {
        PathingUtil.walkToTile(Const.BANK_TILE, 2, false);
        if (BankManager.open(true)) {
            Waiting.waitNormal(500, 175);
        }
    }

    private void openPollBooth() {
        BankManager.close(true);

        if (Utils.clickObject("Poll booth", "Use", false) &&
                NPCInteraction.waitForConversationWindow()) {
            NPCInteraction.handleConversation();
            Timer.waitCondition(() -> Interfaces.get(310, 2, 11) != null, 2500, 4000);
        }
    }

    private void closePollInterface() {
        RSInterface closeButton = Interfaces.findWhereAction("Close", 310);
        if (closeButton != null && closeButton.click()) {
            Timer.waitCondition(() -> Interfaces.get(310) == null, 2500, 4000);
        }
        //if (Interfaces.get(310, 2, 11) != null) {

    }


    private void goToAccountGuide() {
        closePollInterface();
        PathingUtil.localNavigation(Const.ACCOUNT_INFO_TILE);
    }

    private void talkToAccountGuide() {
        closePollInterface();
        if (NpcChat.talkToNPC(3310) &&
                NPCInteraction.waitForConversationWindow()) { // don't check this, it stops it from working
            NPCInteraction.handleConversation();
        }
    }

    private void openTab() {
        int SKILLS_INTERFACE = General.isClientResizable() ? 164 : 548;
        RSInterface skills = Interfaces.findWhereAction("Account Management", SKILLS_INTERFACE);
        if (skills != null && skills.click()) {
            Timer.slowWaitCondition(() -> GameTab.getOpen() ==
                    GameTab.TABS.OPTIONS, 1250, 2000);
        }
    }


    @Override
    public String toString() {
        return "Bank Task";
    }

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return Game.getSetting(Const.GAME_SETTING) >= 500 &&
                Game.getSetting(Const.GAME_SETTING) <= 532;
    }

    @Override
    public void execute() {
        if (Game.getSetting(Const.GAME_SETTING) == 500 || Game.getSetting(Const.GAME_SETTING) == 510) {
            openBank();
        }
        else   if (Game.getSetting(Const.GAME_SETTING) == 520) {
            openPollBooth();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 525) {
            goToAccountGuide();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 530) {
            talkToAccountGuide();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 531) {
            openTab();
        }
        else  if (Game.getSetting(Const.GAME_SETTING) == 532) {
            closePollInterface();
            talkToAccountGuide();
        }
    }
}
