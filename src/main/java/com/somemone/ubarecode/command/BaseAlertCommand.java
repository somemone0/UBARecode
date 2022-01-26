package com.somemone.ubarecode.command;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.somemone.ubarecode.account.Account;
import com.somemone.ubarecode.file.FileHandler;
import net.dv8tion.jda.api.MessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class BaseAlertCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length != 1) return false;
        if (args[0].equals("ba") && args[1].equals("delete") && args.length == 3) {
            if (!sender.isOp()) return false;
            OfflinePlayer op = Bukkit.getOfflinePlayer(args[2]);
            if (op != null && op.hasPlayedBefore()) {
                try {
                    FileHandler.deleteAccount(op.getPlayer().getUniqueId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                return true;
            }
        } // Scans for /ba delete <player>
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (!player.hasPermission("basealert.use")) {
            player.sendMessage(ChatColor.GREEN + " [BaseAlert] " + ChatColor.RED + "You do not have BA! Get BA at store.ro11en.com");
            return true;
        }

        /* Gets a list of accounts to work with */
        List<Account> accounts = null;
        try {
            accounts = FileHandler.getAllAccounts();
        } catch (IOException e) {
            sender.sendMessage(ChatColor.RED + "Critical File Read Error! Contact a moderator!");
            return true;
        }

        if (args[0].equals("generate")) {

            Account account = null;
            for (Account a : accounts) {
                if (a.getOwnerID().equals((player.getUniqueId()))) {
                    account = a;
                }
            }

            Resident resident = TownyAPI.getInstance().getResident(player);
            Nation nation = TownyAPI.getInstance().getResidentNationOrNull(resident);

            if (nation == null) {
                player.sendMessage(ChatColor.GREEN + " [BaseAlert] " + ChatColor.YELLOW + "You bought BA, but you aren't in a nation! I'd join one first.");
                return false;
            }

            if (account == null) {
                account = new Account(player.getUniqueId());
            } else {
                account.generateActivationCode();
            }

            player.sendMessage(ChatColor.GREEN + " [BaseAlert] " + ChatColor.YELLOW + "Code generated: " + ChatColor.GOLD + account.getActivationCode() +
                    ChatColor.GREEN + ". Use this code within 24 hours!");

            try {
                FileHandler.saveAccount(account);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (args[0].equals("everyone")) {

            Account account = null;
            for (Account a : accounts) {
                if (a.getOwnerID().equals((player.getUniqueId()))) {
                    account = a;
                }
            }

            if (account.everyone) {
                account.everyone = false;
                player.sendMessage(ChatColor.GREEN + " [BaseAlert] " + ChatColor.YELLOW + "BaseAlert WILL NOT @everyone in chat");
            } else {
                account.everyone = true;
                player.sendMessage(ChatColor.GREEN + " [BaseAlert] " + ChatColor.YELLOW + "BaseAlert WILL @everyone in chat");
            }

            try {
                FileHandler.saveAccount(account);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (args[0].equals("delete")) {
            try {
                if (FileHandler.hasAccount(player.getUniqueId())) {
                    player.sendMessage(ChatColor.GREEN + " [BaseAlert] " + ChatColor.YELLOW + "Your account has been deleted. Don't worry, as long as you are still subscribed, then you" +
                            " can do /ba generate again.");

                    FileHandler.deleteAccount(player.getUniqueId());
                } else {
                    player.sendMessage(ChatColor.GREEN + " [BaseAlert] " + ChatColor.YELLOW + "You don't have an account registered with us.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            return false;
        }

        return true;
    }
}
