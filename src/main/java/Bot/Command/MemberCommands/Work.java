package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;

import java.util.concurrent.TimeUnit;

public class Work implements ICommand {

   @Override
   public void execute(CommandContext ctx) {
      if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
         return;
      }

      long sleepCooldown = IDataBaseManager.INSTANCE.getCooldown(ctx.getAuthorID(), "sleep") - System.currentTimeMillis();
      int remainingSleep = (int) TimeUnit.MILLISECONDS.toMinutes(sleepCooldown);

      if (sleepCooldown > 0) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca sleeps, it will wake up in **" + (remainingSleep == 1 ? remainingSleep + "** minute" : remainingSleep + "** minutes")).queue();
         return;
      }

      long workCooldown = IDataBaseManager.INSTANCE.getCooldown(ctx.getAuthorID(), "work") - System.currentTimeMillis();
      int remainingWork = (int) TimeUnit.MILLISECONDS.toMinutes(workCooldown);

      if (workCooldown > 0) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca already worked, it has to rest **" + (remainingWork == 1 ? remainingWork + "** minute" : remainingWork + "** minutes") + " to work again").queue();
         return;
      }

      int amountOfFluffies = (int) (Math.random() * 15 + 1);

      IDataBaseManager.INSTANCE.setBalance(ctx.getAuthorID(), amountOfFluffies);
      IDataBaseManager.INSTANCE.setCooldown(ctx.getAuthorID(), "work", System.currentTimeMillis() + 1000L * 60 * 20);

      ctx.getChannel().sendMessage("⛏ You went to work and earned **" + (amountOfFluffies > 1 ? amountOfFluffies + "** fluffies" : amountOfFluffies + "** fluffy")).queue();
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
