package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;

public class Pet implements ICommand {

   @Override
   public void handle(CommandContext commandContext) {
      commandContext.getChannel().sendMessage("https://tenor.com/view/alpaca-small-alpaca-pet-cute-combing-gif-15237621").queue();
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "pet\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Show your alpaca some love and pet him";
   }

   @Override
   public String getName() {
      return "pet";
   }

   @Override
   public Enum<PermissionLevel> getPermissionLevel() {
      return PermissionLevel.MEMBER;
   }
}