package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.TextChannel;

public class WorkCommand implements ICommand {

    @Override
    public void handle(CommandContext commandContext) {
        long memberID = commandContext.getGuild().getMember(commandContext.getAuthor()).getIdLong();
        final TextChannel channel = commandContext.getChannel();
        long cooldown = IDataBaseManager.INSTANCE.getCooldown(memberID, "cooldown_work") - System.currentTimeMillis();

        if (cooldown < 0) {
            long newCooldown = System.currentTimeMillis() + 60000 * 20;
            int amountOfFluffies = (int) Math.round(Math.random() * (15 - 1) + 1);

            IDataBaseManager.INSTANCE.setInventory(memberID, "currency", amountOfFluffies);
            IDataBaseManager.INSTANCE.setCooldown(memberID, "cooldown_work", newCooldown);

            channel.sendMessage("⛏ You went to work and earned **" + amountOfFluffies + "** fluffies").queue();
        } else {
            channel.sendMessage("<:RedCross:782229279312314368> You've already worked, you have to rest **" + Math.round((cooldown / (1000 * 60) % 60)) + "** minutes until you can use this command again").queue();
        }
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
