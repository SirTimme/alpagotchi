package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;

public class Pet implements ICommand {

   @Override
   public void handle(CommandContext commandContext) {
      commandContext.getChannel().sendMessage("https://cdn.discordapp.com/attachments/786319189413330957/786982440471887893/alpacaPet.gif").queue();
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
   public String getPermissionLevel() {
      return "member";
   }
}