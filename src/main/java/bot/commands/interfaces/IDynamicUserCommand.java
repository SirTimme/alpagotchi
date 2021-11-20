package bot.commands.interfaces;

import bot.models.Entry;
import bot.utils.CommandType;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface IDynamicUserCommand extends ISlashCommand {
	Entry execute(final SlashCommandEvent event, final Entry entry);

	@Override
	default CommandType getCommandType() {
		return CommandType.DYNAMIC_USER;
	}
}
