package scripts.Data;

import org.tribot.api2007.Player;

import java.util.concurrent.atomic.AtomicBoolean;

public class Vars {

    private static Vars vars;

    public static Vars get() {
        return vars == null ? vars = new Vars() : vars;
    }


    public static void reset() {
        vars = new Vars();
    }

    public int minLoopSleep = 150;
    public int maxLoopSleep = 400;
    public int mouseSpeed = 100;

    public String accountName = "";

    public boolean disableMusic = false;



}
