package Bot.Command.AdminCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Config;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Decrease implements ICommand {
    private final Timer timer = new Timer();
    private final TimerTask sqlTask = new SQLTask();

    @Override
    public void handle(CommandContext commandContext) {
        final TextChannel channel = commandContext.getChannel();
        final List<String> args = commandContext.getArgs();

        if (args.isEmpty()) {
            channel.sendMessage("<:RedCross:782229279312314368> Missing Arguments").queue();
            return;
        }

        if (!commandContext.getAuthor().getId().equals(Config.get("OWNER_ID"))) {
            channel.sendMessage("<:RedCross:782229279312314368> Only the developer is allowed to use this command").queue();
            return;
        }

        if (args.get(0).equalsIgnoreCase("enable")) {
            this.timer.schedule(this.sqlTask, 0, 7200000);
            channel.sendMessage("<:GreenTick:782229268914372609> Alpacas begin to lose stats over time").queue();
        } else if (args.get(0).equalsIgnoreCase("disable")) {
            this.timer.cancel();
            channel.sendMessage("<:RedCross:782229279312314368> Alpacas stop losing stats over time").queue();
        } else {
            channel.sendMessage("<:RedCross:782229279312314368> Incorrect Arguments").queue();
        }
    }

    @Override
    public String getHelp(CommandContext commandContext) {
        return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "decrease [enable | disable]`\nDetermines if the alpacas lose values over time";
    }

    @Override
    public String getName() {
        return "decrease";
    }
}

class SQLTask extends TimerTask {

    @Override
    public void run() {
        IDataBaseManager.INSTANCE.decreaseValues();
    }
}
