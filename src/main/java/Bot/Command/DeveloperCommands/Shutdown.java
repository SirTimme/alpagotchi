package Bot.Command.DeveloperCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class Shutdown implements ICommand {

   @Override
   public void handle(CommandContext commandContext) {
      Member member = commandContext.getMember();
      TextChannel channel = commandContext.getChannel();

      if (!PermissionLevel.DEVELOPER.hasPerms(member)) {
         channel.sendMessage("<:RedCross:782229279312314368> This is a developer-only command").queue();
         return;
      }

      channel.sendMessage("<:GreenTick:782229268914372609> " + commandContext.getJDA().getSelfUser().getName() + " is shutting down...").queue();
      System.exit(0);
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "shutdown\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Shutdowns the bot";
   }

   @Override
   public String getName() {
      return "shutdown";
   }

   @Override
   public Enum<PermissionLevel> getPermissionLevel() {
      return PermissionLevel.DEVELOPER;
   }
}
