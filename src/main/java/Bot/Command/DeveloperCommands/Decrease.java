package Bot.Command.DeveloperCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Decrease implements ICommand {
   private final Timer timer = new Timer();
   boolean isTimerRunning = false;
   private TimerTask sqlTask;

   @Override
   public void handle(CommandContext commandContext) {
      final TextChannel channel = commandContext.getChannel();
      final List<String> args = commandContext.getArgs();

      if (!PermissionLevel.DEVELOPER.hasPerms(commandContext.getMember())) {
         channel.sendMessage("<:RedCross:782229279312314368> This is a developer-only command").queue();
         return;
      }

      if (args.isEmpty()) {
         channel.sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      if (!(args.get(0).equalsIgnoreCase("enable") || args.get(0).equalsIgnoreCase("disable"))) {
         channel.sendMessage("<:RedCross:782229279312314368> Incorrect arguments").queue();
         return;
      }

      if (isTimerRunning && args.get(0).equalsIgnoreCase("enable")) {
         channel.sendMessage("<:RedCross:782229279312314368> Decreasing is already enabled").queue();
         return;
      }

      if (args.get(0).equalsIgnoreCase("enable")) {
         this.timer.schedule(sqlTask = new TimerTask() {
            @Override
            public void run() {
               IDataBaseManager.INSTANCE.decreaseValues();
               isTimerRunning = true;
            }
         }, 0, 1000 * 7200);
         channel.sendMessage("<:GreenTick:782229268914372609> Alpacas begin to lose stats over time").queue();

      } else {
         this.sqlTask.cancel();
         isTimerRunning = false;
         channel.sendMessage("<:RedCross:782229279312314368> Alpacas stop losing stats over time").queue();
      }
   }

   @Override
   public String getHelp(String prefix) {
      return "`Usage: " + prefix + "decrease [enable | disable]\n" + (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") + "Determines if the alpacas lose values over time";
   }

   @Override
   public String getName() {
      return "decrease";
   }

   @Override
   public Enum<PermissionLevel> getPermissionLevel() {
      return PermissionLevel.DEVELOPER;
   }
}
