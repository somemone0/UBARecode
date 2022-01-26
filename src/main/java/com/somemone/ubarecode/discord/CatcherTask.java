package com.somemone.ubarecode.discord;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class CatcherTask extends BukkitRunnable {

    public ArrayList<DiscordCatcher> catchers = new ArrayList<>();

    @Override
    public void run() {
        ArrayList<DiscordCatcher> trash = new ArrayList<>();
        for (DiscordCatcher catcher : catchers) {
            catcher.time--;
            if (catcher.time == 0) {
                trash.add(catcher);
            }
        }
        catchers.removeAll(trash);
    }

    public boolean contains(String catcherID) {
        for (DiscordCatcher catcher : catchers) {
            if (catcher.channelID.equals(catcherID)) {
                return true;
            }
        }
        return false;
    }

}
