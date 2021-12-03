package de.borekking.bot.listener.button;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ButtonAction {

    private final long id;
    private final Map<Button, Consumer<Member>> actions;

    public ButtonAction(long id) {
        this.id = id;
        this.actions = new HashMap<>();
    }

    public boolean compareID(long id) {
        return this.id == id;
    }

    public Consumer<Member> getAction(Button button) {
        return this.actions.get(button);
    }

    public void addAction(Button button, Consumer<Member> c) {
        this.actions.put(button, c);
    }
}
