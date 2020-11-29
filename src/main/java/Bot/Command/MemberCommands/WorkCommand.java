package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.TextChannel;

public class WorkCommand implements ICommand {
    @Override
    public void handle(CommandContext commandContext) {
        long memberID = commandContext.getGuild().getMember(commandContext.getAuthor()).getIdLong();
        TextChannel channel = commandContext.getChannel();

        String amountOfFluffies = String.valueOf((int)Math.round(Math.random() * (15 - 1) + 1));
        IDataBaseManager.INSTANCE.setCurrency(memberID, amountOfFluffies);

        channel.sendMessage("⛏ You went to work and earned **" + amountOfFluffies + "** fluffies").queue();
    }

    @Override
    public String getHelp(CommandContext commandContext) {
        return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "work`\nWork for a random amount of fluffies";
    }

    @Override
    public String getName() {
        return "work";
    }
}
