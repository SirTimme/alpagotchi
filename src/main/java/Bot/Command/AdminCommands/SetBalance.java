package Bot.Command.AdminCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;

import java.util.List;

public class SetBalance implements ICommand {

   @Override
   public void execute(CommandContext commandContext) {

      if (!PermissionLevel.ADMIN.hasPerms(commandContext.getMember())) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> This is a admin-only command").queue();
         return;
      }

      final List<String> args = commandContext.getArgs();

      if (args.isEmpty() || args.size() < 2) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      if (commandContext.getMessage().getMentionedUsers().isEmpty()) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the mentioned user").queue();
         return;
      }

      final long mentionedUserID = commandContext.getMessage().getMentionedUsers().get(0).getIdLong();

      if (!IDataBaseManager.INSTANCE.isUserInDB(mentionedUserID)) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> The mentioned user does not own a alpaca, he have to use **" + commandContext.getPrefix() + "init** first").queue();
         return;
      }

      final int currentBalance = IDataBaseManager.INSTANCE.getBalance(mentionedUserID);
      int newBalance;

      try {
         newBalance = Integer.parseInt(args.get(1));

      } catch (NumberFormatException error) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the amount of items").queue();
         return;
      }

      IDataBaseManager.INSTANCE.setBalance(mentionedUserID, newBalance - currentBalance);

      commandContext.getChannel().sendMessage("\uD83D\uDCB3 The balance of **" + commandContext.getJDA().getUserById(mentionedUserID).getName() + "** has been set to **" + newBalance + "**").queue();
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "setbalance [@user] [balance]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Sets the balance for the specified user";
   }

   @Override
   public String getName() {
      return "setbalance";
   }

   @Override
   public Enum<PermissionLevel> getPermissionLevel() {
      return PermissionLevel.ADMIN;
   }
}
