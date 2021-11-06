package bot.commands.dev;

import bot.commands.IInfoCommand;
import bot.db.IDatabase;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class Count implements IInfoCommand {
    @Override
    public void execute(SlashCommandEvent event) {
        event.reply("\uD83D\uDC65 There are **" + IDatabase.INSTANCE.getEntries() + "** " +
                            "alpacas in **" + event.getJDA().getGuilds().size() + "** farms by now")
             .queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("count", "Counts all alpacas of Alpagotchi").setDefaultEnabled(false);
    }
}
