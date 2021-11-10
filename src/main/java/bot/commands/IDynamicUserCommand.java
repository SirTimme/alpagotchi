package bot.commands;

import bot.models.Entry;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface IDynamicUserCommand extends ISlashCommand {
	Entry execute(final SlashCommandEvent event, final Entry entry);
}
