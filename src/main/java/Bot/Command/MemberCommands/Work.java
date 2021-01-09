package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;

public class Work implements ICommand {

   @Override
   public void execute(CommandContext commandContext) {

      if (!IDataBaseManager.INSTANCE.isUserInDB(commandContext.getAuthorID())) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + commandContext.getPrefix() + "init** first").queue();
         return;
      }

      long sleepCooldown = IDataBaseManager.INSTANCE.getCooldown(commandContext.getAuthorID(), "sleep") - System.currentTimeMillis();
      int remainingSleep = (int) (sleepCooldown / 60000 % 60);

      if (sleepCooldown > 0) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca sleeps, it will wake up in **" + (remainingSleep == 1 ? remainingSleep + "** minute" : remainingSleep + "** minutes")).queue();
         return;
      }

      long workCooldown = IDataBaseManager.INSTANCE.getCooldown(commandContext.getAuthorID(), "work") - System.currentTimeMillis();
      int remainingWork = (int) (workCooldown / 60000 % 60);

      if (workCooldown > 0) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca already worked, it has to rest **" + (remainingWork == 1 ? remainingWork + "** minute" : remainingWork + "** minutes") + " to work again").queue();
         return;
      }

      long newCooldown = System.currentTimeMillis() + 1200000;
      int amountOfFluffies = (int) (Math.random() * 15 + 1);

      IDataBaseManager.INSTANCE.setInventory(commandContext.getAuthorID(), "currency", amountOfFluffies);
      IDataBaseManager.INSTANCE.setCooldown(commandContext.getAuthorID(), "work", newCooldown);

      commandContext.getChannel().sendMessage("⛏ You went to work and earned **" + (amountOfFluffies > 1 ? amountOfFluffies + "** fluffies" : amountOfFluffies + "** fluffy")).queue();
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
