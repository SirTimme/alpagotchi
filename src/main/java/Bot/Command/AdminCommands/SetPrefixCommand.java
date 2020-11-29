package Bot.Command.AdminCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class SetPrefixCommand implements ICommand {

    @Override
    public void handle(CommandContext commandContext) {
        final TextChannel channel = commandContext.getChannel();
        final List<String> args = commandContext.getArgs();
        final Member member = commandContext.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("<:RedCross:782229279312314368> You are missing the `MANAGE_SERVER` permission!").queue();
            return;
        }

        if (args.isEmpty()) {
            channel.sendMessage("<:RedCross:782229279312314368> Missing Arguments").queue();
            return;
        }

        final String newPrefix = String.join("", args);
        IDataBaseManager.INSTANCE.setPrefix(commandContext.getGuild().getIdLong(), newPrefix);

        channel.sendMessageFormat("<:GreenTick:782229268914372609> New Prefix has been set to **" + newPrefix + "**").queue();
    }

    @Override
    public String getHelp(CommandContext commandContext) {
        return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "setprefix [prefix]`\nSets the prefix for this server";
    }

    @Override
    public String getName() {
        return "setprefix";
    }
}
