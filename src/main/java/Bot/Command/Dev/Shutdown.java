package Bot.Command.Dev;

import Bot.Command.ISlashCommand;
import Bot.Utils.Emote;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Shutdown implements ISlashCommand {
    @Override
    public void execute(SlashCommandEvent event, long authorID) {
        event.reply(Emote.GREENTICK + " **Alpagotchi** is shutting down...").queue();

        event.getJDA().shutdown();
    }
}
