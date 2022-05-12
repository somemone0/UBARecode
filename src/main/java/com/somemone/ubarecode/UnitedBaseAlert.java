package com.somemone.ubarecode;

import com.somemone.ubarecode.command.BaseAdminCommand;
import com.somemone.ubarecode.command.BaseAlertCommand;
import com.somemone.ubarecode.discord.CatcherTask;
import com.somemone.ubarecode.discord.DiscordListener;
import com.somemone.ubarecode.listener.ChunkListener;
import com.somemone.ubarecode.listener.EnemyAddListener;
import com.somemone.ubarecode.listener.EnterChunkListener;
import com.somemone.ubarecode.time.AccountCheckTask;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public final class UnitedBaseAlert extends JavaPlugin {

    public static JDA jda;
    public static File dataFile;
    public static CatcherTask catcherInstance = new CatcherTask();

    @Override
    public void onEnable() {

        dataFile = new File( this.getDataFolder(), "data.json" );

        //First time setup
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
                Files.write(Path.of(dataFile.getPath()), "[]".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JDABuilder builder = JDABuilder.createDefault("ODk5NDg0NTYyMjQ0MTEyNDQ1.YWzcRw.uTkN7lM0yYPtPFLL3HFdctyVdjQ", GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES);
        builder.addEventListeners(new DiscordListener());
        builder.setActivity(Activity.watching("Your base"));
        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        jda.addEventListener(new DiscordListener());

        this.getCommand("basealert").setExecutor(new BaseAlertCommand());
        this.getCommand("baseadmin").setExecutor(new BaseAdminCommand());

        getServer().getPluginManager().registerEvents(new ChunkListener(), this);
        getServer().getPluginManager().registerEvents(new EnemyAddListener(), this);
        getServer().getPluginManager().registerEvents(new EnterChunkListener(), this);

        catcherInstance.runTaskTimer(this, 0L, 20L);
        new AccountCheckTask().runTaskTimer(this, 0L, 1200L);


    }

    @Override
    public void onDisable() {
        jda.shutdown();
    }
}
