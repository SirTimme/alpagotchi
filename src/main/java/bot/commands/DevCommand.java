package bot.commands;

import bot.utils.CommandType;

public abstract class DevCommand extends SlashCommand {
    @Override
    protected CommandType getCommandType() {
        return CommandType.DEV;
    }
}
