package Bot.Command.AdminCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.Permission;

import java.util.List;

public class SetBalance implements ICommand {

   @Override
   public void execute(CommandContext ctx) {

      if (!PermissionLevel.ADMIN.hasPerms(ctx.getMember())) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> This is a **admin-only** command, you are missing the **" + Permission.MANAGE_SERVER.getName() + "** permission").queue();
         return;
      }

      final List<String> args = ctx.getArgs();

      if (args.isEmpty() || args.size() < 2) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      if (ctx.getMessage().getMentionedUsers().isEmpty()) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the mentioned user").queue();
         return;
      }

      final long mentionedUserID = ctx.getMessage().getMentionedUsers().get(0).getIdLong();

      if (!IDataBaseManager.INSTANCE.isUserInDB(mentionedUserID)) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> The mentioned user does not own a alpaca, he have to use **" + ctx.getPrefix() + "init** first").queue();
         return;
      }

      final int currentBalance = IDataBaseManager.INSTANCE.getBalance(mentionedUserID);
      int newBalance;

      try {
         newBalance = Integer.parseInt(args.get(1));

      } catch (NumberFormatException error) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the amount of items").queue();
         return;
      }

      IDataBaseManager.INSTANCE.setBalance(mentionedUserID, newBalance - currentBalance);

      ctx.getChannel().sendMessage("\uD83D\uDCB3 The balance of **" + ctx.getJDA().getUserById(mentionedUserID).getName() + "** has been set to **" + newBalance + "**").queue();
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
