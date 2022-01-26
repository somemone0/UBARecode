package com.somemone.ubarecode.discord;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.somemone.ubarecode.UnitedBaseAlert;
import com.somemone.ubarecode.account.Account;
import com.somemone.ubarecode.account.NotifyEnterType;
import com.somemone.ubarecode.file.FileHandler;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.List;

public class DiscordListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (UnitedBaseAlert.catcherInstance.contains( event.getChannel().getId() )) {
            return;
        }

        UnitedBaseAlert.catcherInstance.catchers.add(new DiscordCatcher(event.getChannel().getId()));

        String message = event.getMessage().getContentDisplay();
        String[] messageArray = message.split(" ");

        if (!messageArray[0].equals("!ba")) return;
        if (messageArray.length == 1) return;

        /* Gets a list of accounts to work with */
        List<Account> accounts = null;
        try {
            accounts = FileHandler.getAllAccounts();
        } catch (IOException e) {
            event.getChannel().sendMessage("**Critical File Read Error! Contact a moderator!**").queue();
            return;
        }


        switch (messageArray[1]) {
            case "register":
                if (messageArray.length != 3) return;
                String codeReceived = messageArray[2];

                accounts.forEach(a -> {
                    if (a.validateActivationCode(codeReceived)) {
                        a.setChannelID( event.getChannel().getId() );
                        a.setGuildID( event.getGuild().getId() );

                        event.getChannel().sendMessage("**RBA successfully bound to channel #" + event.getChannel().getName() + "**").queue();
                        try {
                            FileHandler.saveAccount(a);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                event.getChannel().sendMessage("**Could not bind RBA. The code is probably incorrect**").queue();
                break;
            case "info":
                Account account = null;
                for (Account a : accounts) {
                    if (a == null) {
                        continue;
                    }
                    if (a.getGuildID().equals(event.getGuild().getId())) {
                        account = a;
                    }
                }

                if (account == null) {
                    event.getChannel().sendMessage("**There is no account assigned to this server**").queue();
                    return;
                }

                Resident res = TownyAPI.getInstance().getResident(account.getOwnerID());
                TextChannel channel = UnitedBaseAlert.jda.getTextChannelById(account.getChannelID());
                try {
                    event.getChannel().sendMessage( "**Your account is ran by " + res.getName() + " for " + res.getNation() + "." +
                            "\n It is hosted on channel #" + channel.getName() + "**").queue();
                } catch (TownyException e) {
                    e.printStackTrace();
                }
                break;

            case "setenter":
                Account ac = null;
                for (Account a : accounts) {
                    if (a == null) {
                        continue;
                    }
                    if (a.getGuildID().equals(event.getGuild().getId())) {
                        ac = a;
                    }
                }
                if (messageArray.length != 3) break;

                if (messageArray[2].equals("none")) {
                    ac.setEnterType(NotifyEnterType.NONE);
                } else if (messageArray[2].equals("enemy")) {
                    ac.setEnterType(NotifyEnterType.IF_ENEMY);
                } else if (messageArray[2].equals("all")) {
                    ac.setEnterType(NotifyEnterType.ALL);
                }
                try {
                    FileHandler.saveAccount(ac);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                event.getChannel().sendMessage("**Unknown command**").queue();
                break;
        }


    }

}
