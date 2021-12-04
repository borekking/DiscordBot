package de.borekking.bot.listener;

import de.borekking.bot.Main;
import de.borekking.bot.util.discord.button.ButtonToActionStorage;
import de.borekking.bot.util.discord.button.ButtonManager;
import de.borekking.bot.util.discord.ButtonUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.function.Consumer;

public class ButtonClickListener extends ListenerAdapter {

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        Button button = event.getButton();
        if (button == null) return;

        long id = Main.getButtonManager().getIDFromButton(button);

        Message msg = event.getMessage();
        Member member = event.getMember();
        if (member == null) return;

        ButtonManager manager = Main.getButtonManager();

        ButtonToActionStorage buttonAction = manager.getButtonEvent(member, id);
        if (buttonAction == null) return;

        Consumer<Member> action = buttonAction.getAction(button);
        if (action != null) {
            action.accept(member);
            ButtonUtils.disableButtons(msg);
            manager.removeButtonEvent(member, id);

            // "close" event only if user was able to press button and button was saved in buttonAction
            event.deferEdit().queue();
        }
    }
}
