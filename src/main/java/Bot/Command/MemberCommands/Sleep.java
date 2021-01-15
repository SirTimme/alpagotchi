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

      int energyValue = IDataBaseManager.INSTANCE.getAlpacaValues(commandContext.getAuthorID(), "energy");

      if (energyValue == 100) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> The energy of your alpaca is already at the maximum").queue();
         return;
      }

      final List<String> args = commandContext.getArgs();

      if (args.isEmpty()) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      int sleepDuration;

      try {
         sleepDuration = Integer.parseInt(args.get(0));
      } catch (NumberFormatException error) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the sleep duration").queue();
         return;
      }

      if (sleepDuration > 120) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca can rest max. 2 hours at once").queue();
         return;
      }

      int sleepValue = sleepDuration / 2;

      if (energyValue + sleepValue > 100) {
         sleepCooldown = System.currentTimeMillis() + (1000L * 60 * (100 - energyValue) * 2);
         IDataBaseManager.INSTANCE.setAlpacaValues(commandContext.getAuthorID(), "energy", 100 - energyValue);
      } else {
         sleepCooldown = System.currentTimeMillis() + (1000L * 60 * sleepDuration);
         IDataBaseManager.INSTANCE.setAlpacaValues(commandContext.getAuthorID(), "energy", sleepValue);
      }

      IDataBaseManager.INSTANCE.setCooldown(commandContext.getAuthorID(), "sleep", sleepCooldown);

      commandContext.getChannel().sendMessage("\uD83D\uDCA4 Your alpaca goes to bed for **" + (energyValue + sleepValue > 100 ? (100 - energyValue) * 2 + "** " : sleepDuration + "** ")
            + "minutes and rests well **Energy + " + (energyValue + sleepValue > 100 ? 100 - energyValue + "** " : sleepValue + "** ") + "").queue();
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
