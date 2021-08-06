package bot.commands.dev;

import bot.commands.IInfoCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import static bot.utils.Emote.GREENTICK;

public class Shutdown implements IInfoCommand {
    @Override
    public void execute(SlashCommandEvent event) {
        event.reply(GREENTICK + " **Alpagotchi** is shutting down...").queue();
        event.getJDA().shutdown();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("shutdown", "Shutdowns Alpagotchi").setDefaultEnabled(false);
    }
}
