package de.borekking.bot.util.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.List;

public class ButtonUtils {

    private ButtonUtils() {
    }

    public static void disableButtons(Message msg) {
        List<ActionRow> actionRows = msg.getActionRows();

        for (ActionRow ar : actionRows) {
            for (Button b : ar.getButtons()) {
                String id = b.getId();
                if (id == null) continue;
                ar.updateComponent(id, b.asDisabled());
            }
        }

        msg.editMessageComponents(actionRows).queue();
    }
}
