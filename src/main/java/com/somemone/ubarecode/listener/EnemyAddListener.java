package com.somemone.ubarecode.listener;

import com.palmergames.bukkit.towny.event.NationAddEnemyEvent;
import com.palmergames.bukkit.towny.exceptions.TownyException;
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
            channel.sendMessage(everyone + "**You have been declared war upon by " + event.getNation().getName() + "**").queue();
        }

        if (attackAccount != null) {

            String everyone = "";
            if (attackAccount.everyone) {
                everyone = "@everyone ";
            }

            TextChannel channel = UnitedBaseAlert.jda.getTextChannelById(attackAccount.getChannelID());
            channel.sendMessage(everyone + "**You have declared war on " + event.getEnemy().getName() + "**").queue();
        }

    }

}
