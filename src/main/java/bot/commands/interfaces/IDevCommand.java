package bot.commands.interfaces;

import bot.utils.CommandType;

public interface IDevCommand extends IInfoCommand {
	@Override
	default CommandType getCommandType() {
		return CommandType.DEV;
	}
}
