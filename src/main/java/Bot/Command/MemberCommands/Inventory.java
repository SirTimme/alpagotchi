package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;

import java.util.List;

public class Inventory implements ICommand {

   @Override
   public void execute(CommandContext commandContext) {

      if (!IDataBaseManager.INSTANCE.isUserInDB(commandContext.getAuthorID())) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + commandContext.getPrefix() + "init** first").queue();
         return;
      }

      int amountOfSalad = IDataBaseManager.INSTANCE.getInventory(commandContext.getAuthorID(), "salad");
      int amountOfWaterbottle = IDataBaseManager.INSTANCE.getInventory(commandContext.getAuthorID(), "waterbottle");

      commandContext.getChannel().sendMessage("\uD83D\uDCE6 Your inventory contains **" +
            (amountOfSalad > 1 ? amountOfSalad + "** salads" : amountOfSalad + "** salad") + " and **" +
            (amountOfWaterbottle > 1 ? amountOfWaterbottle + "** waterbottles" : amountOfWaterbottle + "** waterbottle")).queue();
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "inventory\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Displays your inventory with the bought items from the shop";
   }

   @Override
   public String getName() {
      return "inventory";
   }

   @Override
   public Enum<PermissionLevel> getPermissionLevel() {
      return PermissionLevel.MEMBER;
   }

   @Override
   public List<String> getAliases() {
      return List.of("inv");
   }
}

