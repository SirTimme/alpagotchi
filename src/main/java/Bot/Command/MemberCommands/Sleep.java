package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;

import java.util.List;

public class Sleep implements ICommand {

   @Override
   public void execute(CommandContext commandContext) {
      if (!IDataBaseManager.INSTANCE.isUserInDB(commandContext.getAuthorID())) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + commandContext.getPrefix() + "init** first").queue();
         return;
      }

      long sleepCooldown = IDataBaseManager.INSTANCE.getCooldown(commandContext.getAuthorID(), "sleep") - System.currentTimeMillis();

      if (sleepCooldown > 0) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca already sleeps").queue();
         return;
      }

      int energy = IDataBaseManager.INSTANCE.getAlpacaValues(commandContext.getAuthorID(), "energy");

      if (energy == 100) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> The energy of your alpaca is already at the maximum").queue();
         return;
      }

      final List<String> args = commandContext.getArgs();
      int duration;

      try {
         duration = Integer.parseInt(args.get(0));
      } catch (NumberFormatException | IndexOutOfBoundsException error) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the sleep duration").queue();
         return;
      }

      if (duration > 120) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca can rest max. 2 hours at once").queue();
         return;
      }

      int newEnergy = energy + duration / 2 > 100 ? 100 - energy : duration / 2;

      IDataBaseManager.INSTANCE.setAlpacaValues(commandContext.getAuthorID(), "energy", newEnergy);
      IDataBaseManager.INSTANCE.setCooldown(commandContext.getAuthorID(), "sleep", System.currentTimeMillis() + 1000L * 60 * 2 * newEnergy);

      commandContext.getChannel().sendMessage("\uD83D\uDCA4 Your alpaca goes to bed for **" + newEnergy * 2 + "** minutes and rests well **Energy + " + newEnergy + "**").queue();
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "sleep [minutes]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Your alpaca sleeps for the specified time and each 2 minutes equals 1 energy";
   }

   @Override
   public String getName() {
      return "sleep";
   }

   @Override
   public Enum<PermissionLevel> getPermissionLevel() {
      return PermissionLevel.MEMBER;
   }
}
