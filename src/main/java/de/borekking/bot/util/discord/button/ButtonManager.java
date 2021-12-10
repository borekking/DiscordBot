package de.borekking.bot.util.discord.button;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ButtonManager {

    private final List<Long> usedIDs;
    private final Map<Long, List<ButtonToActionStorage>> buttonEvents;

    public ButtonManager() {
        this.buttonEvents = new HashMap<>();
        this.usedIDs = new ArrayList<>();
    }

    // Methode to get a unique id
    public long getNewUniqueID() {
        long id = new Random().nextInt();
        if (!this.usedIDs.contains(id)) return id;
        return this.getNewUniqueID();
    }

    // Methode to apply right syntax to button´s actual ID
    public String[] getIDs(long id, int amount) {
        String[] arr = new String[amount];
        for (int i = 0; i < amount; i++)
            arr[i] = id + "." + i;
        return arr;
    }

    // Methode to get ID from button´s ID
    public long getIDFromButton(Button button) {
        String buttonID = button.getId();
        return  Long.parseLong(buttonID.split("\\.")[0]);
    }

    public void addButtonEvent(Member member, ButtonToActionStorage e) {
        long memberID = member.getIdLong();
        if (this.buttonEvents.containsKey(memberID))
            this.buttonEvents.get(memberID).add(e);
        else
            this.buttonEvents.put(memberID, new ArrayList<>(Collections.singletonList(e)));
    }

    public ButtonToActionStorage getButtonEvent(Member member, long id) {
        List<ButtonToActionStorage> list = this.buttonEvents.get(member.getIdLong());
        if (list == null) return null;

        return list.stream().filter(buttonAction -> buttonAction.compareID(id)).findFirst().orElse(null);
    }

    public void removeButtonEvent(Member member, long id) {
        List<ButtonToActionStorage> list = this.buttonEvents.get(member.getIdLong());
        if (list == null) return;

        list.removeIf(action -> action.compareID(id));

        this.usedIDs.remove(id);
    }

    public void clear() {
        this.buttonEvents.clear();
    }
}
