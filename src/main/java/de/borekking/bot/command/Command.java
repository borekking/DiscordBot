package de.borekking.bot.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public abstract class Command {

    private final String name, description;
    private final Permission[] permissions;
    private OptionData[] options;

    public Command(String name, String description, OptionData[] options, Permission... permissions) {
        this.name = name;
        this.description = description;
        this.options = options;
        this.permissions = permissions;
    }

    public abstract void perform(SlashCommandEvent event);

    public CommandData getCommandData() {
        CommandData commandData = new CommandData(this.name, this.description);
        if (this.options != null)
            commandData.addOptions(this.options);
        return commandData;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public OptionData[] getOptions() {
        return options;
    }

    public Permission[] getPermissions() {
        return permissions;
    }
}
