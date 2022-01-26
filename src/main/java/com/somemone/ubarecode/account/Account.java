package com.somemone.ubarecode.account;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Account {

    private String activationCode;
    private String acExpiration;

    private UUID ownerID;

    private String channelID;
    private String guildID;
    public boolean everyone;
    private NotifyEnterType enterType;

    public Account (UUID ownerID) {
        this.ownerID = ownerID;
        everyone = false;

        channelID = "";
        guildID = "";
        enterType = NotifyEnterType.IF_ENEMY;

        generateActivationCode();
    }

    public void generateActivationCode() {
        this.activationCode = "";
        for (int i = 0; i < 7; i++) this.activationCode += "abcdefghijklmnopqrstuvwxyz1234567890".charAt(  (int) (Math.random() * 36)  );

        this.acExpiration = LocalDateTime.now().plusDays(1L).format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public boolean validateActivationCode(String code) {

        if (!code.equals(activationCode)) return false;

        LocalDateTime ace = LocalDateTime.parse(acExpiration, DateTimeFormatter.ISO_DATE_TIME);
        if (ace.isBefore(LocalDateTime.now())) return false;

        // Resets activation codes
        this.acExpiration = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        activationCode = "lllllllllllllllllllllllllllllllllllll";
        return true;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public NotifyEnterType getType() {return enterType;}

    public UUID getOwnerID() {
        return ownerID;
    }

    public String getChannelID() {
        return channelID;
    }

    public String getGuildID() {
        return guildID;
    }

    public void setEnterType(NotifyEnterType type) {enterType = type;}

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public void setGuildID(String guildID) {
        this.guildID = guildID;
    }

}
