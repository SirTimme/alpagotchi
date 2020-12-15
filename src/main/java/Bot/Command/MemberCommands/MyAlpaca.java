package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Config;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.io.File;
import java.time.Instant;
import java.util.List;

public class MyAlpaca implements ICommand {

    @Override
    public void handle(CommandContext commandContext) {
        File myAlpacaFile = new File("src/main/resources/myAlpaca.gif");
        EmbedBuilder embedBuilder = new EmbedBuilder();
        final Member botCreator = commandContext.getGuild().getMemberById(Config.get("OWNER_ID"));
        final long memberID = commandContext.getMessage().getAuthor().getIdLong();

        embedBuilder.setTitle("" + IDataBaseManager.INSTANCE.getNickname(memberID) + "");
        embedBuilder.addField("Hunger \uD83C\uDF56", IDataBaseManager.INSTANCE.getStatus(memberID, "hunger") + "/100", true);
        embedBuilder.addField("Thirst \uD83C\uDF7C", IDataBaseManager.INSTANCE.getStatus(memberID, "thirst") + "/100", true);
        embedBuilder.addField("Energy \uD83D\uDD0B", IDataBaseManager.INSTANCE.getStatus(memberID, "energy") + "/100", true);
        embedBuilder.setThumbnail(commandContext.getMember().getUser().getAvatarUrl());
        embedBuilder.setFooter("Created by " + botCreator.getEffectiveName(), botCreator.getUser().getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setImage("attachment://myAlpaca.gif");

        commandContext.getChannel().sendFile(myAlpacaFile, "myAlpaca.gif").embed(embedBuilder.build()).queue();
    }

    @Override
    public String getHelp(CommandContext commandContext) {
        return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "myalpaca\n" +
                (this.getAliases().isEmpty() ? "`" : "Aliases: " + this.getAliases() + "`\n") +
                "Shows the stats of your alpaca";
    }

    @Override
    public String getName() {
        return "myalpaca";
    }

    @Override
    public String getPermissionLevel() {
        return "member";
    }

    @Override
    public List<String> getAliases() {
        return List.of("ma", "stats", "alpaca");
    }
}
