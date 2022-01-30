package de.borekking.bot.util.discord.button;

import de.borekking.bot.Main;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.function.Consumer;

public class ConfirmationButtonSender {

    // TODO Make n Buttons possible (ButtonActionSender)

    private final long id;
    private final Button successButton, cancelButton;
    private final Consumer<Member> onSuccess, onCancel;

    public ConfirmationButtonSender(String successTitle, Consumer<Member> onSuccess, String dangerTitle, Consumer<Member> onCancel) {
        ButtonManager manager = Main.getButtonManager();

        // Get new unique id
        this.id = manager.getNewUniqueID();

        // Created buttons with id
        String[] buttonID = manager.getIDs(this.id, 2);
        this.successButton = Button.success(buttonID[0], successTitle);
        this.cancelButton = Button.danger(buttonID[1], dangerTitle);

        // Set consumers/actions
        this.onSuccess = onSuccess;
        this.onCancel = onCancel;
    }

    public void use(Member member) {
        // Create ButtonAction
        ButtonToActionStorage buttonAction = new ButtonToActionStorage(this.id);
        buttonAction.addAction(this.successButton, this.onSuccess);
        buttonAction.addAction(this.cancelButton, this.onCancel);

        Main.getButtonManager().addButtonEvent(member, buttonAction);
    }

    public Button[] getButtons() {
        return new Button[]{this.successButton, this.cancelButton};
    }
}
