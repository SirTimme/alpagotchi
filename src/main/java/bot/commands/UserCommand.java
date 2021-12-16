package bot.commands;

import bot.utils.CommandType;

public abstract class UserCommand extends SlashCommand {
    @Override
    protected CommandType getCommandType() {
        return CommandType.USER;
    }
}
