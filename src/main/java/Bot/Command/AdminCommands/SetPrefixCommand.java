package Bot.Command.AdminCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Constants;
import Bot.Database.SQLiteDataSource;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SetPrefixCommand implements ICommand {

    @Override
    public void handle(CommandContext commandContext) {
        final TextChannel channel = commandContext.getChannel();
        final List<String> args = commandContext.getArgs();
        final Member member = commandContext.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("<:RedCross:782229279312314368> You are missing the `MANAGE_SERVER` permission!").queue();
            return;
        }

        if (args.isEmpty()) {
            channel.sendMessage("<:RedCross:782229279312314368> Missing Arguments").queue();
            return;
        }

        final String newPrefix = String.join("", args);
        updatePrefix(commandContext.getGuild().getIdLong(), newPrefix);

        channel.sendMessageFormat("<:GreenTick:782229268914372609> New Prefix has been set to `%s`", newPrefix).queue();
    }

    @Override
    public String getHelp() {
        return "`Usage: a!setprefix [prefix]`\nSets the prefix for this server";
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    private void updatePrefix(long guildID, String newPrefix) {
        Constants.PREFIXES.put(guildID, newPrefix);

        try (final PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                // language=SQLite
                .prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {

            preparedStatement.setString(1, newPrefix);
            preparedStatement.setString(2, String.valueOf(guildID));

            preparedStatement.executeUpdate();

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }
}
