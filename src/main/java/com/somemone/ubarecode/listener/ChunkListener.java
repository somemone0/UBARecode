package com.somemone.ubarecode.listener;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.somemone.ubarecode.UnitedBaseAlert;
import com.somemone.ubarecode.account.Account;
import com.somemone.ubarecode.account.AccountType;
import com.somemone.ubarecode.file.FileHandler;
import io.github.townyadvanced.flagwar.events.CellAttackEvent;
import io.github.townyadvanced.flagwar.events.CellWonEvent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;

public class ChunkListener implements Listener {

    @EventHandler
    public void onCellAttack (CellAttackEvent event) throws TownyException, IOException {
        Block block = event.getData().getFlagBaseBlock();
        Nation nation = TownyAPI.getInstance().getTownBlock(block.getLocation()).getTown().getNation();

        Nation attackingNation = TownyUniverse.getInstance().getResident(event.getPlayer().getUniqueId()).getNation();

        Account account = FileHandler.getNationAccount(nation.getUUID());
        if (account != null) {

            if (account.getType() == AccountType.FREE) return;

            String everyone = "";
            if (account.everyone) {
                everyone = "@everyone ";
            }

            TextChannel channel = UnitedBaseAlert.jda.getTextChannelById(account.getChannelID());
            channel.sendMessage(everyone + "**A chunk is under attack by " + attackingNation.getName() +
                "\nAt block " + block.getX() + ", " + block.getY() + ", " + block.getZ() + "**").queue();
        }

    }

    @EventHandler
    public void onCellWon (CellWonEvent event) throws TownyException, IOException {
        Block block = event.getCellUnderAttack().getFlagBaseBlock();
        Nation nation = TownyAPI.getInstance().getTownBlock(block.getLocation()).getTown().getNation();

        Account account = FileHandler.getNationAccount(nation.getUUID());
        if (account != null) {

            if (account.getType() == AccountType.FREE) return;

            String everyone = "";
            if (account.everyone) {
                everyone = "@everyone ";
            }

            TextChannel channel = UnitedBaseAlert.jda.getTextChannelById(account.getChannelID());
            channel.sendMessage(everyone + "**A chunk has been taken!" +
                "\nLocation: " + block.getChunk().getX() + ", " + block.getChunk().getZ() + "**" ).queue();
        }
    }
}
