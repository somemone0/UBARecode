package com.somemone.ubarecode.listener;

import com.palmergames.bukkit.towny.event.NationAddEnemyEvent;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.somemone.ro11ensmpcore.events.WarStartEvent;
import com.somemone.ubarecode.UnitedBaseAlert;
import com.somemone.ubarecode.account.Account;
import com.somemone.ubarecode.file.FileHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class EnemyAddListener implements Listener {

    @EventHandler
    public void onNationAddEnemy (NationAddEnemyEvent event) throws IOException, TownyException {

        Account enemyAccount = FileHandler.getNationAccount( event.getEnemy().getUUID() );
        Account attackAccount = FileHandler.getNationAccount( event.getNation().getUUID() );

        if (enemyAccount != null) {

            String everyone = "";
            if (enemyAccount.everyone) {
                everyone = "@everyone ";
            }

            TextChannel channel = UnitedBaseAlert.jda.getTextChannelById(enemyAccount.getChannelID());
            channel.sendMessage(everyone + "**You are now under siege from " + event.getNation().getName() + "**").queue();
        }

        if (attackAccount != null) {

            String everyone = "";
            if (attackAccount.everyone) {
                everyone = "@everyone ";
            }

            TextChannel channel = UnitedBaseAlert.jda.getTextChannelById(attackAccount.getChannelID());
            channel.sendMessage(everyone + "**The siege against " + event.getEnemy().getName() + " has started**").queue();
        }

    }

    @EventHandler
    public void onNationWarStart (WarStartEvent event) throws IOException, TownyException {

        Account enemyAccount = FileHandler.getNationAccount( event.getWar().getNationAttacking().getUUID() );
        Account attackAccount = FileHandler.getNationAccount( event.getWar().getNationDefending().getUUID() );

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a, EEE LLL dd");
        String everyone ="";

        if (enemyAccount != null) {

            if (enemyAccount.everyone) {
                everyone = "@everyone ";
            }

            TextChannel channel = UnitedBaseAlert.jda.getTextChannelById(enemyAccount.getChannelID());
            channel.sendMessage(everyone + "**You have declared war on " + event.getWar().getNationDefending().getName() + ". The war will start at " +
                    dtf.format(event.getWar().getDateTime()) + "**").queue();

        }

        if (attackAccount != null) {

            everyone = "";
            if (attackAccount.everyone) {
                everyone = "@everyone ";
            }

            TextChannel channel = UnitedBaseAlert.jda.getTextChannelById(attackAccount.getChannelID());
            channel.sendMessage(everyone + "**You have declared war upon on by " + event.getWar().getNationAttacking().getName() + ". The war will start at " +
                    dtf.format(event.getWar().getDateTime()) + "**").queue();
        }

    }

}
