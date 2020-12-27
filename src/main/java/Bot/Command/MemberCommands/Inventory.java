package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Inventory implements ICommand {

   @Override
   public void handle(CommandContext commandContext) {
      final TextChannel channel = commandContext.getChannel();
      final long memberID = commandContext.getGuild().getMember(commandContext.getAuthor()).getIdLong();

      int amountOfSalad = IDataBaseManager.INSTANCE.getInventory(memberID, "salad");
      int amountOfWaterbottle = IDataBaseManager.INSTANCE.getInventory(memberID, "waterbottle");

      channel.sendMessage("\uD83D\uDCE6 Your inventory contains **" +
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

