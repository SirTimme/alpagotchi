package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.TextChannel;

public class Work implements ICommand {

    @Override
    public void handle(CommandContext commandContext) {
        long memberID = commandContext.getGuild().getMember(commandContext.getAuthor()).getIdLong();
        final TextChannel channel = commandContext.getChannel();
        long workCooldown = IDataBaseManager.INSTANCE.getCooldown(memberID, "work") - System.currentTimeMillis();
        long sleepCooldown = IDataBaseManager.INSTANCE.getCooldown(memberID, "sleep") - System.currentTimeMillis();

        if (sleepCooldown > 0) {
            channel.sendMessage("<:RedCross:782229279312314368> Your alpaca sleeps, it will wake up in **" + (int)(((sleepCooldown / 1000) / 60) % 60) + "** minutes").queue();
            return;
        }

        IDataBaseManager.INSTANCE.setCooldown(memberID, "sleep", 0);

        if (workCooldown > 0) {
            channel.sendMessage("<:RedCross:782229279312314368> Your alpaca already worked, it has to rest **" + (int)(((workCooldown / 1000) / 60) % 60) + "** minutes to work again").queue();
            return;
        }

        long newCooldown = System.currentTimeMillis() + 1200000;
        int amountOfFluffies = (int)(Math.random() * 15 + 1);

        IDataBaseManager.INSTANCE.setInventory(memberID, "currency", amountOfFluffies);
        IDataBaseManager.INSTANCE.setCooldown(memberID, "work", newCooldown);

        channel.sendMessage("⛏ You went to work and earned **" + (amountOfFluffies > 1 ? amountOfFluffies + "** fluffies" : amountOfFluffies + "** fluffy")).queue();
    }

    @Override
    public String getHelp(String prefix) {
        return "`Usage: " + prefix + "work\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Work for a random amount of fluffies";
    }

    @Override
    public String getName() {
        return "work";
    }

    @Override
    public Enum<PermissionLevel> getPermissionLevel() {
        return PermissionLevel.MEMBER;
    }
}
