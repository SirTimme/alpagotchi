package bot.commands.member;

import bot.commands.IUserCommand;
import bot.models.Entry;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

public class Delete implements IUserCommand {
    @Override
    public Entry execute(SlashCommandEvent event, Entry user) {
        event.reply("⚠ Are you sure you want to delete your data? You **permanently** lose all progress")
             .addActionRow(Button.success("acceptDelete", "Accept"), Button.danger("cancelDelete", "Cancel"))
             .setEphemeral(true)
             .queue();

        return null;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("delete", "Deletes your personal data");
    }
}
