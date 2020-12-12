package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Nick implements ICommand {

   @Override
   public void handle(CommandContext commandContext) {
      final TextChannel channel = commandContext.getChannel();
      final long memberID = commandContext.getGuild().getMember(commandContext.getAuthor()).getIdLong();
      final List<String> args = commandContext.getArgs();

      if (args.isEmpty()) {
         channel.sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      if (args.get(0).length() > 256) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments, the nickname must not exceed **256** characters").queue();
         return;
      }

      String nickname = args.get(0);
      IDataBaseManager.INSTANCE.setNickname(memberID, nickname);
      channel.sendMessage("\uD83D\uDD8A The nickname of your alpaca has been set to **" + nickname + "**").queue();
   }

   @Override
   public String getHelp(CommandContext commandContext) {
      return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "nick [nickname]`\nGives your alpaca a nickname";
   }

   @Override
   public String getName() {
      return "nick";
   }
}