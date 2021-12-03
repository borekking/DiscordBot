package de.borekking.bot.listener.button;

import net.dv8tion.jda.api.entities.Member;

import java.util.*;

public class ButtonManager {

    private final List<Long> usedIDs;
    private final Map<Long, List<ButtonAction>> buttonEvents;

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

    public void addButtonEvent(Member member, ButtonAction e) {
        long memberID = member.getIdLong();
        if (this.buttonEvents.containsKey(memberID))
            this.buttonEvents.get(memberID).add(e);
        else
            this.buttonEvents.put(memberID, new ArrayList<>(Collections.singletonList(e)));
    }

    public ButtonAction getButtonEvent(Member member, long id) {
        List<ButtonAction> list = this.buttonEvents.get(member.getIdLong());
        if (list == null) return null;

        return list.stream().filter(buttonAction -> buttonAction.compareID(id)).findFirst().orElse(null);
    }

    public void removeButtonEvent(Member member, long id) {
        List<ButtonAction> list = this.buttonEvents.get(member.getIdLong());
        if (list == null) return;

        list.removeIf(action -> action.compareID(id));

        this.usedIDs.remove(id);
    }

    public void clear() {
        this.buttonEvents.clear();
    }
}
