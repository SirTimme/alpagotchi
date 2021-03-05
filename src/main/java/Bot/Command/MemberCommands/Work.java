package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Utils.PermissionLevel;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.util.concurrent.TimeUnit;

public class Work implements ICommand {
   @Override
   public void execute(CommandContext ctx) throws PermissionException {
      if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
         return;
      }

      final int energy = IDataBaseManager.INSTANCE.getAlpacaValues(ctx.getAuthorID(), "energy");
      if (energy < 10) {
         ctx.getChannel().sendMessage("\uD83E\uDD71 Your alpaca is too tired to work, let it rest first with **" + ctx.getPrefix() + "sleep**").queue();
         return;
      }

      final long sleepCooldown = IDataBaseManager.INSTANCE.getCooldown(ctx.getAuthorID(), "sleep") - System.currentTimeMillis();
      if (sleepCooldown > 0) {
         int remainingSleep = (int) TimeUnit.MILLISECONDS.toMinutes(sleepCooldown);
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca sleeps, it will wake up in **" + (remainingSleep == 1 ? remainingSleep + "** minute" : remainingSleep + "** minutes")).queue();
         return;
      }

      final long workCooldown = IDataBaseManager.INSTANCE.getCooldown(ctx.getAuthorID(), "work") - System.currentTimeMillis();
      if (workCooldown > 0) {
         int remainingWork = (int) TimeUnit.MILLISECONDS.toMinutes(workCooldown);
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca already worked, it has to rest **" + (remainingWork == 1 ? remainingWork + "** minute" : remainingWork + "** minutes") + " to work again").queue();
         return;
      }

      final int amountOfFluffies = (int) (Math.random() * 15 + 1);
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
