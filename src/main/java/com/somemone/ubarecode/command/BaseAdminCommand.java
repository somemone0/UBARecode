package com.somemone.ubarecode.command;

import com.somemone.ubarecode.account.Account;
import com.somemone.ubarecode.account.AccountType;
import com.somemone.ubarecode.file.FileHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class BaseAdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.isOp()) return false;

        if (args.length == 0) return false;

        if (args[0].equals("renew")) {
            if (args.length <= 1) return false;

            Account account = null;
            try {
                account = FileHandler.getAccount(UUID.fromString(args[1]));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (account == null) return false;

            account.renewSubscription();

            try {
                FileHandler.saveAccount(account);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (args[0].equals("create")) {
            if (args.length <= 1) return false;

            Account account = new Account(UUID.fromString(args[1]), AccountType.PREMIUM);
            if (Bukkit.getOfflinePlayer(UUID.fromString(args[1])).isOnline()) {
                Bukkit.getPlayer(UUID.fromString(args[1])).sendMessage(ChatColor.GREEN + "Your account has been created! Use /ba generate to create a code to type into Discord");
            }

            try {
                FileHandler.saveAccount(account);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
