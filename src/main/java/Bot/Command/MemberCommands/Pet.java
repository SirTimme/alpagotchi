package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.TextChannel;

public class Pet implements ICommand {

   @Override
   public void handle(CommandContext commandContext) {
      final TextChannel channel = commandContext.getChannel();
      final long memberID = commandContext.getMember().getIdLong();
      final int joy = IDataBaseManager.INSTANCE.getAlpacaValues(memberID, "joy");
      int amountOfJoy = (int) (Math.random() * 10 + 1);

      if (amountOfJoy + joy > 100) {
         channel.sendMessage("<:RedCross:782229279312314368> The joy of your alpaca is already at the maximum").queue();
         return;
      }

      IDataBaseManager.INSTANCE.setAlpacaValues(memberID, "joy", amountOfJoy);

      channel.sendMessage("\uD83E\uDD99 Your alpaca loves to spend time with you **Joy + " + amountOfJoy + "**").queue();
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "pet\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Pet your alpaca and increases his joy";
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