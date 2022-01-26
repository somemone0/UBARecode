package com.somemone.ubarecode.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.somemone.ubarecode.UnitedBaseAlert;
import com.somemone.ubarecode.account.Account;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FileHandler {

    public static Account getAccount (UUID playerId) throws IOException {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        List<Account> accounts = gson.fromJson(Files.readString(UnitedBaseAlert.dataFile.toPath()), new TypeToken<List<Account>>() {}.getType());

        Account account = null;

        for (Account a : accounts) {
            if (a.getOwnerID().equals(playerId)) account = a;
        }

        return account;

    }

    public static Account getNationAccount (UUID nationId) throws IOException, TownyException {

        List<Account> accounts = getAllAccounts();
        for (Account account : accounts) {
            Resident res = TownyAPI.getInstance().getResident(account.getOwnerID());
            if (res.hasNation()) {
                Nation pNation = res.getNation();
                if (pNation.getUUID().equals( nationId )) {
                    return account;
                }
            }
        }

        return null;
    }

    public static List<Account> getAllAccounts () throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        List<Account> accounts = gson.fromJson(Files.readString(UnitedBaseAlert.dataFile.toPath()), new TypeToken<List<Account>>() {}.getType());
        if (accounts == null) {
            accounts = new ArrayList<>();
        }

        return accounts;
    }

    public static void saveAccount (Account account) throws IOException {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        List<Account> accounts = gson.fromJson(Files.readString(UnitedBaseAlert.dataFile.toPath()), new TypeToken<List<Account>>() {}.getType());
        List<Account> trash = new ArrayList<>();

        accounts.forEach((a) -> {
            if (a.getOwnerID().equals(account.getOwnerID())) {
                trash.add(a);
            }
        });

        accounts.removeAll(trash);

        accounts.add(account);

        String json = gson.toJson(accounts);

        Files.write(UnitedBaseAlert.dataFile.toPath(), Collections.singleton(json));

    }

    public static void deleteAccount (UUID ownerID) throws IOException {
        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();

        List<Account> accounts = gson.fromJson(Files.readString(UnitedBaseAlert.dataFile.toPath()), new TypeToken<List<Account>>() {}.getType());

        List<Account> trash = new ArrayList<>();

        accounts.forEach((a) -> {
            if (a.getOwnerID().equals(ownerID)) {
                trash.add(a);
            }
        });

        accounts.removeAll(trash);

        String json = gson.toJson(accounts);

        Files.write(UnitedBaseAlert.dataFile.toPath(), Collections.singleton(json));builder.setPrettyPrinting();

    }

    public static boolean hasAccount (UUID ownerID) throws IOException {
        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();

        List<Account> accounts = gson.fromJson(Files.readString(UnitedBaseAlert.dataFile.toPath()), new TypeToken<List<Account>>() {}.getType());

        for (Account a : accounts) {
            if (a.getOwnerID().equals(ownerID)) {
                return true;
            }
        }
        return false;
    }

}
