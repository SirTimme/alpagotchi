package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.TextChannel;

public class Work implements ICommand {

    @Override
    public void handle(CommandContext commandContext) {
        long memberID = commandContext.getGuild().getMember(commandContext.getAuthor()).getIdLong();
        final TextChannel channel = commandContext.getChannel();
        long cooldown = IDataBaseManager.INSTANCE.getCooldown(memberID, "work") - System.currentTimeMillis();

        if (cooldown > 0) {
            channel.sendMessage("<:RedCross:782229279312314368> You've already worked, you have to rest **" + (int) (((cooldown / 1000) / 60) % 60) + "** minutes to work again").queue();
            return;
        }

        long newCooldown = System.currentTimeMillis() + 1200000;
        int amountOfFluffies = (int)(Math.random() * 15 + 1);

        IDataBaseManager.INSTANCE.setInventory(memberID, "currency", amountOfFluffies);
        IDataBaseManager.INSTANCE.setCooldown(memberID, "work", newCooldown);

        if (amountOfFluffies == 1) {
            channel.sendMessage("⛏ You went to work and earned **" + amountOfFluffies + "** fluffy").queue();
        } else {
            channel.sendMessage("⛏ You went to work and earned **" + amountOfFluffies + "** fluffies").queue();
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

    @Override
    public String getPermissionLevel() {
        return "member";
    }
}
