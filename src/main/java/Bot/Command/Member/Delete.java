package Bot.Command.Member;

import Bot.Command.ISlashCommand;
import Bot.Models.User;
import Bot.Database.IDatabase;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;

public class Delete implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        User user = IDatabase.INSTANCE.getUser(authorID);

        if (user == null) {
            event.reply(Emote.REDCROSS + " You don't own an alpaca, use **/init** first")
                 .setEphemeral(true)
                 .queue();
            return;
        }

        event.reply("⚠ Are you sure you want to delete your data? You **permanently** lose all progress")
             .addActionRow(
                     Button.success("acceptDelete", "Accept"),
                     Button.danger("cancelDelete", "Cancel")
             )
             .setEphemeral(true)
             .queue();
    }
}
