package de.borekking.bot.util.discord.button;

import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ButtonIDCreator {

    /*
     * Syntax:
     * <prefix>-<id>-<count>
     *
     */

    // Prefix
    private final String prefix;
    // Used ids
    private final List<Long> ids;

    public ButtonIDCreator(String prefix) {
        this.prefix = prefix.replaceAll("-", "");
        this.ids = new ArrayList<>();
    }

    public void setUsed(long id) {
        this.ids.add(id);
    }

    public boolean containsID(long id) {
        return this.ids.contains(id);
    }

    // Methode to get a unique id
    public long getNewUniqueID() {
        long id = Math.abs(new Random().nextInt());
        if (!this.containsID(id)) {
            this.ids.add(id);
            return id;
        }
        return this.getNewUniqueID();
    }

    // Methode to apply right syntax to button´s actual ID
    public String[] getIDs(long id, int amount) {
        String[] arr = new String[amount];
        for (int i = 0; i < amount; i++)
            arr[i] = this.prefix + "-" + id + "-" + i;
        return arr;
    }

    public void remove(Long l) {
        this.ids.remove(l);
    }

    // Methods to get Prefix from button
    public static String getPrefixFromButtonID(String buttonID) {
        return getButtonIDElement(buttonID, 0);
    }

    public static String getPrefixFromButton(Button button) {
        return getPrefixFromButtonID(button.getId());
    }

    // Methods to get ID from button / button ID
    public static long getIDFromButtonID(String buttonID) {
        String buttonIDElement = getButtonIDElement(buttonID, 1);
        try {
            return buttonIDElement != null ? Long.parseLong(buttonIDElement) : -1L;
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    public static long getIDFromButton(Button button) {
        return getIDFromButtonID(button.getId());
    }

    // Methode to get count from button´s ID
    public static int getCountFromButton(Button button) {
        return getCountFromButtonID(button.getId());
    }

    public static int getCountFromButtonID(String buttonID) {
        String buttonIDElement = getButtonIDElement(buttonID, 1);
        try {
            return buttonIDElement != null ? Integer.parseInt(buttonIDElement) : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String getButtonIDElement(String buttonID, int pos) {
        String[] args = buttonID.split("-");
        if (args.length != 3) return null;
        return args[pos];
    }

    public String getPrefix() {
        return prefix;
    }
}
