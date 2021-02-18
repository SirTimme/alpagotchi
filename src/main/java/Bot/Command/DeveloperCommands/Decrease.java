package Bot.Command.DeveloperCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Command.PermissionLevel;
import Bot.Database.IDataBaseManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Decrease implements ICommand {
   private final Timer timer = new Timer();
   private boolean isTimerRunning = false;
   private TimerTask sqlTask;

   @Override
   public void execute(CommandContext ctx) {
      if (!PermissionLevel.DEVELOPER.hasPermission(ctx.getMember())) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> This is a **developer-only** command").queue();
         return;
      }

      final List<String> args = ctx.getArgs();

      if (args.isEmpty()) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Missing arguments").queue();
         return;
      }

      if (!(args.get(0).equalsIgnoreCase("enable") || args.get(0).equalsIgnoreCase("disable"))) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Incorrect arguments").queue();
         return;
      }

      if (isTimerRunning && args.get(0).equalsIgnoreCase("enable")) {
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Decreasing is already enabled").queue();
         return;
      }

      if (args.get(0).equalsIgnoreCase("enable")) {
         this.timer.schedule(sqlTask = new TimerTask() {
            @Override
            public void run() {
               IDataBaseManager.INSTANCE.decreaseValues();
               isTimerRunning = true;
            }
         }, 1000 * 7200, 1000 * 7200);
         ctx.getChannel().sendMessage("<:GreenTick:782229268914372609> Alpacas begin to lose stats over time").queue();
      } else {
         this.sqlTask.cancel();
         isTimerRunning = false;
         ctx.getChannel().sendMessage("<:RedCross:782229279312314368> Alpacas stop losing stats over time").queue();
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
