package com.somemone.ubarecode.time;

import com.somemone.ubarecode.UnitedBaseAlert;
import com.somemone.ubarecode.account.Account;
import com.somemone.ubarecode.account.AccountType;
import com.somemone.ubarecode.file.FileHandler;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class AccountCheckTask extends BukkitRunnable {
    @Override
    public void run() {
        try {

            List<Account> accounts = FileHandler.getAllAccounts();
            boolean changed = false;

            for (Account account : accounts) {
                if (account.getType() == AccountType.PREMIUM) {
                    if (account.getAccountExpiration().isAfter(LocalDateTime.now())) {

                        account.setType(AccountType.FREE);
                        TextChannel channel = UnitedBaseAlert.jda.getTextChannelById(account.getChannelID());
                        channel.sendMessage("**Your subscription has ran out. Your account will now only have FREE features. If this message was made" +
                                " in error, please contact a moderator**").queue();

                        changed = true;

                    }
                }
            }

            if (changed) {
                FileHandler.saveAllAccounts(accounts);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
