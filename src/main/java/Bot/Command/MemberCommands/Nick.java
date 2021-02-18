package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;

public class Nick implements ICommand {
   @Override
   public void execute(CommandContext ctx) {
      if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
         return;
      }

      final String nickname;

      try {
         nickname = ctx.getArgs().get(0);
      } catch (IndexOutOfBoundsException error) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the specified nickname").queue();
         return;
      }
      IDataBaseManager.INSTANCE.setNickname(ctx.getAuthorID(), nickname);

      ctx.getChannel().sendMessage("\uD83D\uDD8A The nickname of your alpaca has been set to **" + nickname + "**").queue();
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "nick [nickname]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Gives your alpaca a nickname";
   }

   @Override
   public String getName() {
      return "nick";
   }

   @Override
   public Enum<PermissionLevel> getPermissionLevel() {
      return PermissionLevel.MEMBER;
   }
}