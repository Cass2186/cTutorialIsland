package scripts;

import dax.api_lib.WebWalkerServerApi;
import dax.api_lib.models.DaxCredentials;
import dax.api_lib.models.DaxCredentialsProvider;
import dax.teleports.Teleport;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Game;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.antiban.PlayerPreferences;
import scripts.Data.Const;
import scripts.Data.Vars;
import scripts.Steps.*;
import scripts.Tasks.Task;
import scripts.Tasks.TaskSet;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@ScriptManifest(name = "cTutorial Island", authors = {"Cass2186"}, category = "Testing")
public class cTutorialIsland extends Script implements Painting, Starting, Ending, Arguments {

    public static AtomicBoolean isRunning = new AtomicBoolean(true);
    public static String status = "Initializing";


    @Override
    public void onStart() {
        AntiBan.create();

        Mouse.setSpeed(Vars.get().mouseSpeed);
        General.println("[Debug]: Setting mouse speed to " + Vars.get().mouseSpeed);

        Teleport.blacklistTeleports(Teleport.values());

        WebWalkerServerApi.getInstance().setDaxCredentialsProvider(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjXXzL5DeSiPf", " PUBLIC-KEY");
            }
        });

    }

    @Override
    public void run() {
        double preference = PlayerPreferences.preference(
                "antiban.double", g -> g.uniform(0.2, 1.0));
        Log.log("[Debug]: Unique Antiban Modifier is: " + preference);
        Utils.FACTOR =preference;
        TaskSet tasks = new TaskSet(
            new MakeCharacter(),
                new SurvivalGuide(),
                new CookingGuide(),
                new QuestGuide(),
                new MiningGuide(),
                new CombatInstructor(),
                new BankTask(),
                new PrayerTask(),
                new MagicTask()
        );

        isRunning.set(true);

        while (isRunning.get()) {
            General.sleep(Vars.get().minLoopSleep, Vars.get().maxLoopSleep);
            Task task = tasks.getValidTask();
            if (task != null) {
                status = task.toString();
                task.execute();
            }
            if (Game.getSetting(Const.GAME_SETTING) == 1000)
                break;
        }
    }

    @Override
    public void onPaint(Graphics g) {
        java.util.List<String> myString = new ArrayList<>(Arrays.asList(
                "cTutorial Island v0.1",
                "Running For: " + Timing.msToString(getRunningTime()),
                "Task: " + status,
                "Game setting 281: " + Game.getSetting(281)
        ));
        PaintUtil.createPaint(g, myString.toArray(String[]::new));
    }

    @Override
    public void passArguments(HashMap<String, String> hashMap) {

    }

    @Override
    public void onEnd() {

    }
}
