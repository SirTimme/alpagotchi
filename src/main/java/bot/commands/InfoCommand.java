package bot.commands;

import bot.utils.CommandType;

public abstract class InfoCommand extends SlashCommand {
    @Override
    protected CommandType getCommandType() {
        return CommandType.INFO;
    }
}
