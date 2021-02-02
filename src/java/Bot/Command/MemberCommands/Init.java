package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;

import java.util.List;

public class Init implements ICommand {

   @Override
   public void execute(CommandContext commandContext) {

      if (IDataBaseManager.INSTANCE.isUserInDB(commandContext.getAuthorID())) {
         commandContext.getChannel().sendMessage("<:RedCross:782229279312314368> Your alpaca has already been set up").queue();
         return;
      }

      IDataBaseManager.INSTANCE.createDBEntry(commandContext.getAuthorID());

      commandContext.getChannel().sendMessage("<:GreenTick:782229268914372609> Your alpaca has been set up").queue();
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "init\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Initialize your alpaca in the database";
   }

   @Override
   public String getName() {
      return "init";
   }

   @Override
   public Enum<PermissionLevel> getPermissionLevel() {
      return PermissionLevel.MEMBER;
   }

   @Override
   public List<String> getAliases() {
      return List.of("setup");
   }
}
