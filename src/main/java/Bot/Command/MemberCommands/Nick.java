package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;

import java.util.List;

public class Nick implements ICommand {

   @Override
   public void execute(CommandContext ctx) {
      if (!IDataBaseManager.INSTANCE.isUserInDB(ctx.getAuthorID())) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + ctx.getPrefix() + "init** first").queue();
         return;
      }

      final List<String> args = ctx.getArgs();

      if (args.isEmpty()) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the specified nickname").queue();
         return;
      }

      if (args.get(0).length() > 256) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> The nickname must not exceed **256** characters").queue();
         return;
      }

      String nickname = args.get(0);
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