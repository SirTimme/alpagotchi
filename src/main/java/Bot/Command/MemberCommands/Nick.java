package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;

import java.util.List;

public class Nick implements ICommand {

   @Override
   public void execute(CommandContext commandContext) {
      if (!IDataBaseManager.INSTANCE.isUserInDB(commandContext.getAuthorID())) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> You do not own a alpaca, use **" + commandContext.getPrefix() + "init** first").queue();
         return;
      }

      final List<String> args = commandContext.getArgs();

      if (args.isEmpty()) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Could not resolve the specified nickname").queue();
         return;
      }

      if (args.get(0).length() > 256) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> The nickname must not exceed **256** characters").queue();
         return;
      }

      String nickname = args.get(0);
      IDataBaseManager.INSTANCE.setNickname(commandContext.getAuthorID(), nickname);

      commandContext.getChannel().sendMessage("\uD83D\uDD8A The nickname of your alpaca has been set to **" + nickname + "**").queue();
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