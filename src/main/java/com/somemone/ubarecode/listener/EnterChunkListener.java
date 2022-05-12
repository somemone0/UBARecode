package com.somemone.ubarecode.listener;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.event.PlayerEnterTownEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.somemone.ubarecode.UnitedBaseAlert;
import com.somemone.ubarecode.account.Account;
import com.somemone.ubarecode.account.AccountType;
import com.somemone.ubarecode.account.NotifyEnterType;
import com.somemone.ubarecode.file.FileHandler;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;

public class EnterChunkListener implements Listener {

    @EventHandler
    public void onPlayerEnter (PlayerEnterTownEvent event) throws TownyException, IOException {

        if (!event.getEnteredtown().hasNation()) return;
        Nation nation = event.getEnteredtown().getNation();
        Account account = FileHandler.getNationAccount(nation.getUUID());
        if (account == null) return;
        String message = "";

        if (account.getType() == AccountType.FREE) return;

        Resident res = TownyUniverse.getInstance().getResident(event.getPlayer().getUniqueId());
        if (account.getType().equals( NotifyEnterType.ALL )) {
            sendTheMessage(account, "**Player " + res.getName() + " has entered town " + event.getEnteredtown().getName() + "!**");
            return;
        }

        if (!res.hasNation()) return;
        Nation enteredNation = res.getNation();

        if (enteredNation.getEnemies().contains(nation) && account.getType().equals(NotifyEnterType.IF_ENEMY)) {
            sendTheMessage(account, "**Enemy player " + res.getName() + " has entered town " + event.getEnteredtown().getName() + "!**");
        }
    }

    private void sendTheMessage (Account account, String message) { // i am very lazy
        TextChannel channel = UnitedBaseAlert.jda.getTextChannelById(account.getChannelID());
        channel.sendMessage(message).queue();
    }
}
