package de.borekking.bot.listener;

import de.borekking.bot.Main;
import de.borekking.bot.util.discord.ButtonUtils;
import de.borekking.bot.util.discord.button.ButtonIDCreator;
import de.borekking.bot.util.discord.button.ButtonManager;
import de.borekking.bot.util.discord.button.ButtonToActionStorage;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.function.Consumer;

public class ButtonClickListener extends ListenerAdapter {

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        Button button = event.getButton();
        if (button == null) return;
        String buttonID = button.getId();
        if (buttonID == null) return;

        Member member = event.getMember();
        if (member == null) return;
        Message msg = event.getMessage();

        if (!(event.getChannel() instanceof TextChannel)) return;

        String prefix = ButtonIDCreator.getPrefixFromButton(button);
        long id = ButtonIDCreator.getIDFromButton(button);
        boolean deferEdit = false;

        // Check in ButtonManager
        if (ButtonManager.BUTTON_PREFIX.equals(prefix)) {
            ButtonManager buttonManager = Main.getButtonManager();
            if (buttonManager.containsID(id)) {
                ButtonToActionStorage buttonAction = buttonManager.getButtonEvent(member, id);
                if (buttonAction == null) return;

                Consumer<Member> action = buttonAction.getAction(button);
                if (action != null) {
                    action.accept(member);
                    ButtonUtils.disableButtons(msg);
                    buttonManager.removeButtonEvent(member, id);

                    deferEdit = true;
                }
            }
        }

        // "close" event only if user was able to press button and button was saved in buttonAction
        if (deferEdit) event.deferEdit().queue();
    }
}
