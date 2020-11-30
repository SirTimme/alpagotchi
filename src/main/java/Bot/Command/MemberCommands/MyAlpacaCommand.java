package Bot.Command.MemberCommands;

import Bot.Command.CommandContext;
import Bot.Command.ICommand;
import Bot.Config;
import Bot.Database.IDataBaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.io.File;
import java.time.Instant;

public class MyAlpacaCommand implements ICommand {

    @Override
    public void handle(CommandContext commandContext) {
        File alpacaFile = new File("src\\main\\resources\\alpaka.gif");
        EmbedBuilder embedBuilder = new EmbedBuilder();
        final Member botCreator = commandContext.getGuild().getMemberById(Config.get("OWNER_ID"));
        final long memberID = commandContext.getMessage().getAuthor().getIdLong();

        embedBuilder.setTitle("" + commandContext.getMember().getEffectiveName() + "'s Alpaca");
        embedBuilder.addField("Hunger \uD83C\uDF56", IDataBaseManager.INSTANCE.getAlpacaStats(memberID, "hunger") + "/100", true);
        embedBuilder.addField("Thirst \uD83C\uDF7C", IDataBaseManager.INSTANCE.getAlpacaStats(memberID, "thirst") + "/100", true);
        embedBuilder.addField("Energy \uD83D\uDD0B", IDataBaseManager.INSTANCE.getAlpacaStats(memberID, "energy") + "/100", true);
        embedBuilder.setThumbnail(commandContext.getMember().getUser().getAvatarUrl());
        embedBuilder.setFooter("Created by " + botCreator.getEffectiveName(), botCreator.getUser().getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setImage("attachment://alpaca.gif");

        commandContext.getChannel().sendFile(alpacaFile, "alpaca.gif").embed(embedBuilder.build()).queue();
    }

    @Override
    public String getHelp(CommandContext commandContext) {
        return "`Usage: " + IDataBaseManager.INSTANCE.getPrefix(commandContext.getGuild().getIdLong()) + "myalpaca`\nShows the stats of your alpaca";
    }

    @Override
    public String getName() {
        return "myalpaca";
    }
}
