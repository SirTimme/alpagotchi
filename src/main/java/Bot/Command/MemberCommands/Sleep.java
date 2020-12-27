package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Sleep implements ICommand {

   @Override
   public void handle(CommandContext commandContext) {
      final TextChannel channel = commandContext.getChannel();
      final long memberID = commandContext.getMember().getIdLong();
      final List<String> args = commandContext.getArgs();
      long sleepCooldown = IDataBaseManager.INSTANCE.getCooldown(memberID, "sleep") - System.currentTimeMillis();
      int sleepDuration;

      if (sleepCooldown > 0) {
         channel.sendMessage("<:RedCross:782229279312314368> Your alpaca already sleeps").queue();
         return;
      }

      if (args.isEmpty()) {
         channel.sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      try {
         sleepDuration = Integer.parseInt(args.get(0));

      } catch (NumberFormatException error) {
         channel.sendMessage("<:RedCross:782229279312314368> Could not resolve the sleep duration").queue();
         return;
      }

      if (sleepDuration > 120) {
         channel.sendMessage("<:RedCross:782229279312314368> Your alpaca can rest max. 2 hours at once").queue();
         return;
      }

      int sleepValue = sleepDuration / 2;
      int energyValue = IDataBaseManager.INSTANCE.getAlpacaValues(memberID, "energy");

      if (energyValue + sleepValue > 100) {
         channel.sendMessage("<:RedCross:782229279312314368> Your alpaca is well rested and dont need sleep").queue();
         return;
      }

      sleepCooldown = System.currentTimeMillis() + (1000L * 60 * sleepDuration);

      IDataBaseManager.INSTANCE.setAlpacaValues(memberID, "energy", sleepValue);
      IDataBaseManager.INSTANCE.setCooldown(memberID, "sleep", sleepCooldown);

      channel.sendMessage("\uD83D\uDCA4 Your alpaca goes to bed for **" + sleepDuration + "** minutes and rests well **Energy + " + sleepValue + "**").queue();
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "sleep [durationInMinutes]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Your alpaca sleeps for the specified time and each 2 minutes equals 1 energy";
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
